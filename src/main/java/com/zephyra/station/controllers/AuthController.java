package com.zephyra.station.controllers;

import com.zephyra.station.dto.SupabaseUserDTO;
import com.zephyra.station.service.SupabaseAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SupabaseAuthService supabaseAuthService;

    @PostMapping("/register")
    public Mono<String> register(@RequestBody SupabaseUserDTO request) {
        return supabaseAuthService.registerUser(request.getEmail(), request.getPassword(),request.getUsername());
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestBody SupabaseUserDTO request) {
        return supabaseAuthService.loginUser(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<String>> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body("Missing refresh_token"));
        }

        return supabaseAuthService.refreshAccessToken(refreshToken)
                .map(tokenResponse -> ResponseEntity.ok().body(tokenResponse))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh_token")));
    }
    @PostMapping("/forgotpass")
    public Mono<ResponseEntity<String>> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        return supabaseAuthService.sendPasswordResetEmail(email)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }
}
