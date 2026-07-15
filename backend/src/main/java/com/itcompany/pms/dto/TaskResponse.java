package com.itcompany.pms.dto;

import com.itcompany.pms.entity.TaskStatus;

public record TaskResponse(Long id, String title, String description, Long projectId, Long createdById, TaskStatus status) {
}
