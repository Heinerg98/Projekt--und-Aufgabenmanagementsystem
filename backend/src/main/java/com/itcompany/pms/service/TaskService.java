package com.itcompany.pms.service;

import com.itcompany.pms.dto.CreateTaskRequest;
import com.itcompany.pms.dto.ProjectProgressResponse;
import com.itcompany.pms.dto.TaskResponse;
import com.itcompany.pms.entity.*;
import com.itcompany.pms.exception.ForbiddenException;
import com.itcompany.pms.exception.NotFoundException;
import com.itcompany.pms.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    public TaskService(TaskRepository taskRepository, ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
    }

    public TaskResponse createTask(User actor, Long projectId, CreateTaskRequest request) {
        Project project = projectService.getProject(projectId);
        boolean isLead = project.getProjectLead().getId().equals(actor.getId());
        boolean isMember = projectService.isMember(project, actor);
        if (!isLead && !isMember) {
            throw new ForbiddenException("Keine Berechtigung für dieses Projekt");
        }
        Task task = new Task();
        task.setProject(project);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCreatedBy(actor);
        task.setStatus(TaskStatus.OFFEN);
        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> listByProject(User actor, Long projectId) {
        Project project = projectService.getProject(projectId);
        if (actor.getRole() != Role.ADMIN && !project.getProjectLead().getId().equals(actor.getId()) && !projectService.isMember(project, actor)) {
            throw new ForbiddenException("Keine Leseberechtigung für Projekt");
        }
        return taskRepository.findByProject(project).stream().map(this::toResponse).toList();
    }

    public TaskResponse updateStatus(User actor, Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Aufgabe nicht gefunden"));
        Project project = task.getProject();
        if (!project.getProjectLead().getId().equals(actor.getId()) && !projectService.isMember(project, actor)) {
            throw new ForbiddenException("Keine Berechtigung zum Statuswechsel");
        }
        task.setStatus(status);
        return toResponse(taskRepository.save(task));
    }

    public ProjectProgressResponse progress(User actor, Long projectId) {
        Project project = projectService.getProject(projectId);
        if (!project.getProjectLead().getId().equals(actor.getId())) {
            throw new ForbiddenException("Nur Projektleiter darf Fortschritt sehen");
        }
        long total = taskRepository.countByProject(project);
        if (total == 0) {
            return new ProjectProgressResponse(projectId, 0);
        }
        long done = taskRepository.countByProjectAndStatus(project, TaskStatus.ERLEDIGT);
        return new ProjectProgressResponse(projectId, ((double) done / total) * 100);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getProject().getId(), task.getCreatedBy().getId(), task.getStatus());
    }
}
