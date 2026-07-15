package com.itcompany.pms.controller;

import com.itcompany.pms.dto.AddMemberRequest;
import com.itcompany.pms.dto.CreateProjectRequest;
import com.itcompany.pms.dto.ProjectResponse;
import com.itcompany.pms.dto.UserResponse;
import com.itcompany.pms.service.AuthService;
import com.itcompany.pms.service.ProjectService;
import com.itcompany.pms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final AuthService authService;
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(AuthService authService, ProjectService projectService, UserService userService) {
        this.authService = authService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public List<ProjectResponse> list(@RequestHeader("X-Auth-Token") String token) {
        return projectService.listVisibleProjects(authService.requireUser(token));
    }

    @PostMapping
    public ProjectResponse create(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody CreateProjectRequest request) {
        return projectService.createProject(authService.requireUser(token), request);
    }

    @PatchMapping("/{id}/archive")
    public ProjectResponse archive(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id) {
        return projectService.archiveProject(authService.requireUser(token), id);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Map<String, String>> addMember(@RequestHeader("X-Auth-Token") String token,
                                                          @PathVariable Long id,
                                                          @Valid @RequestBody AddMemberRequest request) {
        projectService.addMember(authService.requireUser(token), id, request.userId());
        return ResponseEntity.ok(Map.of("message", "Mitglied hinzugefügt"));
    }

    @GetMapping("/admin/users")
    public List<UserResponse> usersForMemberSelection(@RequestHeader("X-Auth-Token") String token) {
        return userService.listUsers(authService.requireUser(token));
    }
}
