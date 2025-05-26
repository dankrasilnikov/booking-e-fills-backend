package com.zephyra.station.service;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.zephyra.station.models.User;
import com.zephyra.station.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Service
public class SupabaseAuthService {

    private final WebClient webClient;
    @Autowired
    private UserRepository userRepository;
    private final String supabaseKey; // сохраняем отдельно ключ

    public SupabaseAuthService(
            @Value("${supabase.api.url}") String supabaseUrl,
            @Value("${supabase.api.key}") String supabaseKey) {
        this.supabaseKey = supabaseKey;
        this.webClient = WebClient.builder()
                .baseUrl(supabaseUrl + "/auth/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
                .defaultHeader("apikey", supabaseKey)
                .build();
    }

public Mono<String> registerUser(String email, String password, String username) {
    if (email == null || email.isBlank() ||
            password == null || password.isBlank() ||
            username == null || username.isBlank()) {
        return Mono.error(new IllegalArgumentException("Email, password, and username must not be empty"));
    }
    return Mono.fromCallable(() -> {
                Optional<User> existingUser = userRepository.findByEmail(email);
                if (existingUser.isPresent()) {
                    throw new RuntimeException("User with this email already exists");
                }
                return null;

            })
            .then(
                    webClient.post()
                            .uri("/signup")
                            .bodyValue("""
                                {
                                    "email": "%s",
                                    "password": "%s",
                                    "username": "%s"
                                }
                                """.formatted(email, password,username))
                            .retrieve()
                            .bodyToMono(String.class)
                            .flatMap(response -> {
                                // Парсим JSON-ответ
                                JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
                                String supabaseId = jsonResponse.get("id").getAsString(); // Берём поле "id"

                                User newUser = new User();
                                newUser.setEmail(email);
                                newUser.setSupabaseId(supabaseId); // сохраняем supabase id
                                newUser.setUsername(username);
                                userRepository.save(newUser);
                                return Mono.just(response);
                            })
            );
}

    public Mono<String> loginUser(String email, String password) {
        return webClient.post()
                .uri("/token?grant_type=password")
                .bodyValue("""
                        {
                            "email": "%s",
                            "password": "%s"
                        }
                        """.formatted(email, password))
                .retrieve()
                .bodyToMono(String.class);
    }
    public Mono<String> refreshAccessToken(String refreshToken) {
        return webClient.post()
                .uri("/token?grant_type=refresh_token")
                .bodyValue("""
                    {
                        "refresh_token": "%s"
                    }
                    """.formatted(refreshToken))
                .retrieve()
                .bodyToMono(String.class);
    }
    public Mono<String> changeUserPassword(String bearerToken, String newPassword) {
        return webClient.put()
                .uri("/user")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .bodyValue("""
                {
                    "password": "%s"
                }
                """.formatted(newPassword))
                .retrieve()
                .bodyToMono(String.class);
    }
    public Mono<String> sendPasswordResetEmail(String email) {
        String redirectUrl = "http://localhost:3000/forgotpass";
        return webClient.post()
                .uri("/recover")
                .header("redirect_to", redirectUrl)  // Добавляем в заголовок
                .bodyValue("""
                {
                    "email": "%s"
                }
                """.formatted(email))
                .retrieve()
                .bodyToMono(String.class);
    }
}