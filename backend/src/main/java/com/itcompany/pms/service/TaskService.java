package com.itcompany.pms.service;

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

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
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
    }
}
