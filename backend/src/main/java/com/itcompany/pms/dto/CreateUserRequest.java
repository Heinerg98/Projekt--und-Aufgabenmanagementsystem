package com.itcompany.pms.dto;

import com.itcompany.pms.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(@NotBlank String username, @NotBlank String password, @NotNull Role role) {
}
