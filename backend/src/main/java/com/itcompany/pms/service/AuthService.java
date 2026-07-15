package com.itcompany.pms.service;

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

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

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
        tokens.put(token, user.getId());
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
        Long userId = tokens.get(token);
        if (userId == null) {
            return Optional.empty();
        }
        return userRepository.findById(userId);
    }
}
