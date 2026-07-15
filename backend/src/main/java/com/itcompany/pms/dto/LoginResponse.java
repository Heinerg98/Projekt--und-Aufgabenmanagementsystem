package com.itcompany.pms.dto;

import com.itcompany.pms.entity.Role;

public record LoginResponse(String token, String username, Role role) {
}
