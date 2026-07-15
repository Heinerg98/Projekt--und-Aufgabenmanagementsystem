package com.itcompany.pms.service;

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
import java.util.List;
import java.util.Set;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
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
    }
}
