package com.zephyra.station.controllers;

import com.zephyra.station.models.User;
import com.zephyra.station.repository.UserRepository;
import com.zephyra.station.service.SupabaseAuthService;
import com.zephyra.station.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    SupabaseAuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getUserProfileByUsername(@RequestParam("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String supabaseId = jwt.getClaimAsString("sub");
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!optionalUser.get().getSupabaseId().equals(supabaseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return userRepository.findBySupabaseId(supabaseId)
                .map(user -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("username", user.getUsername());
                    response.put("role", user.getRole().name());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/profile/changepass")
    public Mono<ResponseEntity<String>> changePassword(
           @RequestHeader("Authorization") String bearerToken,
            @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        return authService.changeUserPassword(bearerToken, newPassword)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }
    @PostMapping("/profile/changeusername")
    public Mono<ResponseEntity<Void>> changeUsername(@RequestBody Map<String, String> body) {
        String newUsername = body.get("newUsername");
        return userService.changeUsername(newUsername)
                .map(success -> ResponseEntity.ok().<Void>build())
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
