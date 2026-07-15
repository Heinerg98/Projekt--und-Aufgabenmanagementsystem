package com.itcompany.pms.dto;

import com.itcompany.pms.entity.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(@NotNull Role role) {
}
