package com.itcompany.pms.service;

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

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
}
