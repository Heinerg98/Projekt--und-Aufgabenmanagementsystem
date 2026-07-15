package com.itcompany.pms.dto;

import com.itcompany.pms.entity.Role;

public record UserResponse(Long id, String username, Role role, boolean active) {
}
