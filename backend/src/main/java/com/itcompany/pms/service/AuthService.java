package com.itcompany.pms.service;

<<<<<<< HEAD
import com.itcompany.pms.dto.LoginRequest;
import com.itcompany.pms.dto.LoginResponse;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.exception.UnauthorizedException;
import com.itcompany.pms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final long tokenTtlSeconds;
    private final Map<String, SessionToken> tokenStore = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${auth.token-ttl-seconds:3600}") long tokenTtlSeconds) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenTtlSeconds = tokenTtlSeconds;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .filter(User::isActive)
                .orElseThrow(() -> new UnauthorizedException("Ungültige Zugangsdaten"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Ungültige Zugangsdaten");
        }

        String token = issueToken(user, Duration.ofSeconds(tokenTtlSeconds));
        return new LoginResponse(token, user.getUsername(), user.getRole());
    }

    public String issueToken(User user, Duration ttl) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, new SessionToken(user.getId(), Instant.now().plus(ttl)));
        return token;
    }

    public void logout(String token) {
        tokenStore.remove(token);
    }

    public User requireUser(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("Fehlender Token");
        }

        SessionToken sessionToken = tokenStore.get(token);
        if (sessionToken == null || sessionToken.expiresAt().isBefore(Instant.now())) {
            tokenStore.remove(token);
            throw new UnauthorizedException("Token ungültig oder abgelaufen");
        }

        return userRepository.findById(sessionToken.userId())
                .orElseThrow(() -> new UnauthorizedException("Benutzer nicht gefunden"));
    }

    private record SessionToken(Long userId, Instant expiresAt) {}
=======
import com.itcompany.pms.dto.AuthResponse;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.exception.UnauthorizedException;
import com.itcompany.pms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Duration;
import java.time.Instant;

@Service
public class AuthService {
    private static final Duration TOKEN_LIFETIME = Duration.ofHours(8);

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, SessionToken> tokens = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UnauthorizedException("Ungültige Zugangsdaten"));

        if (!user.isActive() || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Ungültige Zugangsdaten");
        }

        String token = UUID.randomUUID().toString();
        tokens.put(token, new SessionToken(user.getId(), Instant.now().plus(TOKEN_LIFETIME)));
        return new AuthResponse(token, userService.toDto(user));
    }

    public void logout(String token) {
        if (token != null) {
            tokens.remove(token);
        }
    }

    public Optional<User> findUserByToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        SessionToken sessionToken = tokens.get(token);
        if (sessionToken == null) {
            return Optional.empty();
        }

        if (sessionToken.expiresAt().isBefore(Instant.now())) {
            tokens.remove(token);
            return Optional.empty();
        }
        return userRepository.findById(sessionToken.userId());
    }

    private record SessionToken(Long userId, Instant expiresAt) {
    }
>>>>>>> origin/main
}
