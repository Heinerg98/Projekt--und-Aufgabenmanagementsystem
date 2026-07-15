package com.itcompany.pms.dto;

import com.itcompany.pms.entity.ProjectStatus;

public record ProjectResponse(Long id, String name, String description, Long projectLeadId, ProjectStatus status) {
}
