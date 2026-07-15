package com.itcompany.pms.controller;

import com.itcompany.pms.dto.ProjectDTO;
import com.itcompany.pms.dto.UserDTO;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.service.ProjectService;
import com.itcompany.pms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public List<ProjectDTO> list(Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return projectService.findVisibleProjects(currentUser);
    }

    @GetMapping("/{id}")
    public ProjectDTO get(Principal principal, @PathVariable Long id) {
        User currentUser = userService.findByUsername(principal.getName());
        return projectService.findById(currentUser, id);
    }

    @PostMapping
    public ProjectDTO create(Principal principal, @Valid @RequestBody ProjectDTO dto) {
        User currentUser = userService.findByUsername(principal.getName());
        return projectService.create(currentUser, dto);
    }

    @PutMapping("/{id}")
    public ProjectDTO update(Principal principal, @PathVariable Long id, @Valid @RequestBody ProjectDTO dto) {
        User currentUser = userService.findByUsername(principal.getName());
        return projectService.update(currentUser, id, dto);
    }

    @PostMapping("/{id}/archive")
    public ProjectDTO archive(Principal principal, @PathVariable Long id) {
        User currentUser = userService.findByUsername(principal.getName());
        return projectService.archive(currentUser, id);
    }

    @PostMapping("/{id}/members")
    public void addMember(Principal principal, @PathVariable Long id, @RequestBody Map<String, Long> request) {
        User currentUser = userService.findByUsername(principal.getName());
        projectService.addMember(currentUser, id, request.get("userId"));
    }

    @GetMapping("/{id}/members")
    public List<UserDTO> members(Principal principal, @PathVariable Long id) {
        User currentUser = userService.findByUsername(principal.getName());
        return projectService.getMembers(currentUser, id);
    }
}
