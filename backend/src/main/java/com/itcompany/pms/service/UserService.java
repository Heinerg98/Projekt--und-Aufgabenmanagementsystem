package com.itcompany.pms.service;

<<<<<<< HEAD
import com.itcompany.pms.dto.CreateUserRequest;
import com.itcompany.pms.dto.UserResponse;
import com.itcompany.pms.entity.Role;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.exception.BadRequestException;
import com.itcompany.pms.exception.ForbiddenException;
import com.itcompany.pms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
=======
import com.itcompany.pms.dto.UserDTO;
import com.itcompany.pms.entity.Role;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.exception.BadRequestException;
import com.itcompany.pms.exception.NotFoundException;
import com.itcompany.pms.exception.UnauthorizedException;
import com.itcompany.pms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
>>>>>>> origin/main

import java.util.List;

@Service
<<<<<<< HEAD
=======
@Transactional
>>>>>>> origin/main
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

<<<<<<< HEAD
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
=======
    public List<UserDTO> findAll(User currentUser) {
        requireAdmin(currentUser);
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    public UserDTO create(User currentUser, UserDTO dto) {
        requireAdmin(currentUser);
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username existiert bereits");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("E-Mail existiert bereits");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("Passwort ist erforderlich");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return toDto(userRepository.save(user));
    }

    public UserDTO update(User currentUser, Long userId, UserDTO dto) {
        requireAdmin(currentUser);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("Benutzer nicht gefunden"));

        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return toDto(userRepository.save(user));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Benutzer nicht gefunden"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("Benutzer nicht gefunden"));
    }

    public UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    private void requireAdmin(User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Nur Admin erlaubt");
        }
    }
>>>>>>> origin/main
}
