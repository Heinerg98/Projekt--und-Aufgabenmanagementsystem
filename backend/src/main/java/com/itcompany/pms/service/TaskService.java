package com.itcompany.pms.service;

<<<<<<< HEAD
import com.itcompany.pms.dto.CreateTaskRequest;
import com.itcompany.pms.dto.ProjectProgressResponse;
import com.itcompany.pms.dto.TaskResponse;
import com.itcompany.pms.entity.*;
import com.itcompany.pms.exception.ForbiddenException;
import com.itcompany.pms.exception.NotFoundException;
import com.itcompany.pms.repository.TaskRepository;
import org.springframework.stereotype.Service;
=======
import com.itcompany.pms.dto.TaskDTO;
import com.itcompany.pms.entity.Project;
import com.itcompany.pms.entity.Task;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.exception.NotFoundException;
import com.itcompany.pms.exception.UnauthorizedException;
import com.itcompany.pms.repository.ProjectMemberRepository;
import com.itcompany.pms.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
>>>>>>> origin/main

import java.util.List;

@Service
<<<<<<< HEAD
=======
@Transactional
>>>>>>> origin/main
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
<<<<<<< HEAD

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
=======
    private final ProjectMemberRepository projectMemberRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository,
                       ProjectService projectService,
                       ProjectMemberRepository projectMemberRepository,
                       UserService userService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.projectMemberRepository = projectMemberRepository;
        this.userService = userService;
    }

    public List<TaskDTO> findByProject(User currentUser, Long projectId) {
        Project project = projectService.getAccessibleProject(currentUser, projectId);
        return taskRepository.findByProject(project).stream().map(this::toDto).toList();
    }

    public TaskDTO findById(User currentUser, Long taskId) {
        Task task = getAccessibleTask(currentUser, taskId);
        return toDto(task);
    }

    public TaskDTO create(User currentUser, Long projectId, TaskDTO dto) {
        Project project = projectService.getAccessibleProject(currentUser, projectId);
        requireProjectMember(currentUser, project);

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setProject(project);
        task.setCreatedBy(currentUser);
        if (dto.getAssignedToId() != null) {
            task.setAssignedTo(userService.findById(dto.getAssignedToId()));
        }
        return toDto(taskRepository.save(task));
    }

    public TaskDTO update(User currentUser, Long taskId, TaskDTO dto) {
        Task task = getAccessibleTask(currentUser, taskId);
        requireProjectMember(currentUser, task.getProject());

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        if (dto.getAssignedToId() != null) {
            task.setAssignedTo(userService.findById(dto.getAssignedToId()));
        } else {
            task.setAssignedTo(null);
        }
        task.touchUpdatedAt();

        return toDto(taskRepository.save(task));
    }

    public TaskDTO updateStatus(User currentUser, Long taskId, TaskDTO dto) {
        Task task = getAccessibleTask(currentUser, taskId);
        requireProjectMember(currentUser, task.getProject());

        task.setStatus(dto.getStatus());
        task.touchUpdatedAt();
        return toDto(taskRepository.save(task));
    }

    private Task getAccessibleTask(User currentUser, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Aufgabe nicht gefunden"));
        projectService.getAccessibleProject(currentUser, task.getProject().getId());
        return task;
    }

    private void requireProjectMember(User currentUser, Project project) {
        boolean allowed = project.getProjectManager().getId().equals(currentUser.getId())
            || projectMemberRepository.existsByProjectAndUser(project, currentUser);
        if (!allowed) {
            throw new UnauthorizedException("Nur Projektmitglieder dürfen Aufgaben verwalten");
        }
    }

    private TaskDTO toDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setProjectId(task.getProject().getId());
        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getId());
            dto.setAssignedToName(task.getAssignedTo().getUsername());
        }
        return dto;
>>>>>>> origin/main
    }
}
