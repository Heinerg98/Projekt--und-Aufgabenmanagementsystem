package com.itcompany.pms.service;

import com.itcompany.pms.dto.CreateUserRequest;
import com.itcompany.pms.dto.UserResponse;
import com.itcompany.pms.entity.Role;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.exception.BadRequestException;
import com.itcompany.pms.exception.ForbiddenException;
import com.itcompany.pms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(User actor, CreateUserRequest request) {
        ensureAdmin(actor);
        userRepository.findByUsername(request.username()).ifPresent(existing -> {
            throw new BadRequestException("Benutzername bereits vergeben");
        });
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setActive(true);
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public List<UserResponse> listUsers(User actor) {
        ensureAdmin(actor);
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse updateRole(User actor, Long userId, Role role) {
        ensureAdmin(actor);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Benutzer nicht gefunden"));
        user.setRole(role);
        return toResponse(userRepository.save(user));
    }

    private void ensureAdmin(User actor) {
        if (actor.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Nur Admin erlaubt");
        }
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.isActive());
    }
}
