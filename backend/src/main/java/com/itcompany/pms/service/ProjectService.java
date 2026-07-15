package com.itcompany.pms.service;

<<<<<<< HEAD
import com.itcompany.pms.dto.CreateProjectRequest;
import com.itcompany.pms.dto.ProjectResponse;
import com.itcompany.pms.entity.*;
import com.itcompany.pms.exception.BadRequestException;
import com.itcompany.pms.exception.ForbiddenException;
import com.itcompany.pms.exception.NotFoundException;
import com.itcompany.pms.repository.ProjectMemberRepository;
import com.itcompany.pms.repository.ProjectRepository;
import com.itcompany.pms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
=======
import com.itcompany.pms.dto.ProjectDTO;
import com.itcompany.pms.dto.UserDTO;
import com.itcompany.pms.entity.*;
import com.itcompany.pms.exception.NotFoundException;
import com.itcompany.pms.exception.UnauthorizedException;
import com.itcompany.pms.repository.ProjectMemberRepository;
import com.itcompany.pms.repository.ProjectRepository;
import com.itcompany.pms.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
>>>>>>> origin/main
import java.util.List;
import java.util.Set;

@Service
<<<<<<< HEAD
=======
@Transactional
>>>>>>> origin/main
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
<<<<<<< HEAD
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponse createProject(User actor, CreateProjectRequest request) {
        if (actor.getRole() != Role.PROJEKTLEITER) {
            throw new ForbiddenException("Nur Projektleiter dürfen Projekte erstellen");
        }
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setProjectLead(actor);
        project.setStatus(ProjectStatus.AKTIV);
        return toResponse(projectRepository.save(project));
    }

    public List<ProjectResponse> listVisibleProjects(User actor) {
        if (actor.getRole() == Role.ADMIN) {
            return projectRepository.findAll().stream().map(this::toResponse).toList();
        }
        if (actor.getRole() == Role.PROJEKTLEITER) {
            return projectRepository.findByProjectLead(actor).stream().map(this::toResponse).toList();
        }
        List<ProjectMember> memberships = projectMemberRepository.findByUser(actor);
        Set<Long> ids = new HashSet<>();
        memberships.forEach(m -> ids.add(m.getProject().getId()));
        return projectRepository.findAllById(ids).stream().map(this::toResponse).toList();
    }

    public ProjectResponse archiveProject(User actor, Long projectId) {
        Project project = getProject(projectId);
        if (!project.getProjectLead().getId().equals(actor.getId())) {
            throw new ForbiddenException("Nur verantwortlicher Projektleiter darf archivieren");
        }
        project.setStatus(ProjectStatus.ARCHIVIERT);
        return toResponse(projectRepository.save(project));
    }

    public void addMember(User actor, Long projectId, Long userId) {
        Project project = getProject(projectId);
        if (!project.getProjectLead().getId().equals(actor.getId())) {
            throw new ForbiddenException("Nur verantwortlicher Projektleiter darf Mitglieder hinzufügen");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Benutzer nicht gefunden"));
        projectMemberRepository.findByProjectAndUser(project, user).ifPresent(pm -> {
            throw new BadRequestException("Mitglied bereits vorhanden");
        });
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        projectMemberRepository.save(member);
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Projekt nicht gefunden"));
    }

    public boolean isMember(Project project, User user) {
        return projectMemberRepository.findByProjectAndUser(project, user).isPresent();
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getProjectLead().getId(), project.getStatus());
=======
    private final TaskRepository taskRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMemberRepository projectMemberRepository,
                          TaskRepository taskRepository,
                          UserService userService) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<ProjectDTO> findVisibleProjects(User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) {
            return projectRepository.findAll().stream().map(this::toDto).toList();
        }

        Set<Project> visible = new LinkedHashSet<>(projectRepository.findByProjectManager(currentUser));
        projectMemberRepository.findByUser(currentUser).forEach(member -> visible.add(member.getProject()));
        return visible.stream().map(this::toDto).toList();
    }

    public ProjectDTO findById(User currentUser, Long id) {
        Project project = getAccessibleProject(currentUser, id);
        return toDto(project);
    }

    public ProjectDTO create(User currentUser, ProjectDTO dto) {
        if (currentUser.getRole() != Role.PROJEKTLEITER) {
            throw new UnauthorizedException("Nur Projektleiter dürfen Projekte anlegen");
        }

        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setProjectManager(currentUser);
        project.setStatus(ProjectStatus.AKTIV);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        return toDto(projectRepository.save(project));
    }

    public ProjectDTO update(User currentUser, Long id, ProjectDTO dto) {
        Project project = getProjectForManager(currentUser, id);
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setUpdatedAt(LocalDateTime.now());
        return toDto(projectRepository.save(project));
    }

    public ProjectDTO archive(User currentUser, Long id) {
        Project project = getProjectForManager(currentUser, id);
        project.setStatus(ProjectStatus.ARCHIVIERT);
        project.setUpdatedAt(LocalDateTime.now());
        return toDto(projectRepository.save(project));
    }

    public void addMember(User currentUser, Long projectId, Long userId) {
        Project project = getProjectForManager(currentUser, projectId);
        User memberUser = userService.findById(userId);

        if (!projectMemberRepository.existsByProjectAndUser(project, memberUser)) {
            ProjectMember member = new ProjectMember();
            member.setProject(project);
            member.setUser(memberUser);
            projectMemberRepository.save(member);
        }
    }

    public List<UserDTO> getMembers(User currentUser, Long projectId) {
        Project project = getAccessibleProject(currentUser, projectId);
        return projectMemberRepository.findByProject(project).stream()
            .map(ProjectMember::getUser)
            .map(userService::toDto)
            .toList();
    }

    public Project getAccessibleProject(User currentUser, Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Projekt nicht gefunden"));

        if (currentUser.getRole() == Role.ADMIN || project.getProjectManager().getId().equals(currentUser.getId())) {
            return project;
        }

        if (projectMemberRepository.existsByProjectAndUser(project, currentUser)) {
            return project;
        }

        throw new UnauthorizedException("Kein Zugriff auf dieses Projekt");
    }

    private Project getProjectForManager(User currentUser, Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Projekt nicht gefunden"));

        if (currentUser.getRole() == Role.ADMIN) {
            return project;
        }

        if (!project.getProjectManager().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Nur zuständiger Projektleiter erlaubt");
        }
        return project;
    }

    public ProjectDTO toDto(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus());
        dto.setProjectManagerId(project.getProjectManager().getId());
        dto.setProjectManagerName(project.getProjectManager().getUsername());

        long totalTasks = taskRepository.countByProject(project);
        long completed = taskRepository.countByProjectAndStatus(project, TaskStatus.ERLEDIGT);
        dto.setTotalTasks(totalTasks);
        dto.setCompletedTasks(completed);
        dto.setProgress(totalTasks == 0 ? 0 : (int) Math.round((completed * 100.0) / totalTasks));

        List<Long> memberIds = projectMemberRepository.findByProject(project)
            .stream()
            .map(member -> member.getUser().getId())
            .toList();
        dto.setMemberIds(memberIds);

        return dto;
>>>>>>> origin/main
    }
}
