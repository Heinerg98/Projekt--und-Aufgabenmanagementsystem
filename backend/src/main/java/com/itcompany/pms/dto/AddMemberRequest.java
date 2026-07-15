package com.itcompany.pms.dto;

import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(@NotNull Long userId) {
}
