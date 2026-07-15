package com.itcompany.pms.controller;

<<<<<<< HEAD
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

=======
import com.itcompany.pms.dto.ProjectDTO;
import com.itcompany.pms.dto.UserDTO;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.service.ProjectService;
import com.itcompany.pms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
>>>>>>> origin/main
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

<<<<<<< HEAD
    private final AuthService authService;
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(AuthService authService, ProjectService projectService, UserService userService) {
        this.authService = authService;
=======
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
>>>>>>> origin/main
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
<<<<<<< HEAD
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
=======
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
>>>>>>> origin/main
    }
}
