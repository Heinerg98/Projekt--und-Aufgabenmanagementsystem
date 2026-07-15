package com.itcompany.pms.controller;

import com.itcompany.pms.dto.LoginRequest;
import com.itcompany.pms.dto.LoginResponse;
import com.itcompany.pms.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader(name = "X-Auth-Token", required = false) String token) {
        if (token != null && !token.isBlank()) {
            authService.logout(token);
        }
        return ResponseEntity.ok(Map.of("message", "Abgemeldet"));
    }

    @GetMapping("/me")
    public Map<String, Object> me(@RequestHeader("X-Auth-Token") String token) {
        var user = authService.requireUser(token);
        return Map.of("id", user.getId(), "username", user.getUsername(), "role", user.getRole());
    }
}
