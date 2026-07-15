package com.itcompany.pms.controller;

<<<<<<< HEAD
import com.itcompany.pms.dto.LoginRequest;
import com.itcompany.pms.dto.LoginResponse;
import com.itcompany.pms.service.AuthService;
=======
import com.itcompany.pms.dto.AuthRequest;
import com.itcompany.pms.dto.AuthResponse;
import com.itcompany.pms.service.AuthService;
import com.itcompany.pms.service.UserService;
>>>>>>> origin/main
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import java.util.Map;

=======
>>>>>>> origin/main
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
<<<<<<< HEAD

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
=======
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "X-Auth-Token", required = false) String token) {
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("X-Auth-Token") String token) {
        return authService.findUserByToken(token)
            .map(userService::toDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(401).build());
>>>>>>> origin/main
    }
}
