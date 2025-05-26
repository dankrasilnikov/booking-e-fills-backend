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
    public ResponseEntity<Map<String, String>> getUserProfileBySupabaseId(
            @RequestParam("supabaseId") String supabaseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String requesterSupabaseId = jwt.getClaimAsString("sub");

        if (!supabaseId.equals(requesterSupabaseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return userService.getUserProfileIfAuthorized(supabaseId);
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
