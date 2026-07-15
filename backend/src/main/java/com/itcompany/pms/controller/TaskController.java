package com.itcompany.pms.controller;

<<<<<<< HEAD
import com.itcompany.pms.dto.CreateTaskRequest;
import com.itcompany.pms.dto.ProjectProgressResponse;
import com.itcompany.pms.dto.TaskResponse;
import com.itcompany.pms.dto.UpdateTaskStatusRequest;
import com.itcompany.pms.service.AuthService;
import com.itcompany.pms.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final AuthService authService;
    private final TaskService taskService;

    public TaskController(AuthService authService, TaskService taskService) {
        this.authService = authService;
        this.taskService = taskService;
    }

    @PostMapping("/projects/{projectId}/tasks")
    public TaskResponse createTask(@RequestHeader("X-Auth-Token") String token,
                                   @PathVariable Long projectId,
                                   @Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(authService.requireUser(token), projectId, request);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public List<TaskResponse> listTasks(@RequestHeader("X-Auth-Token") String token,
                                        @PathVariable Long projectId) {
        return taskService.listByProject(authService.requireUser(token), projectId);
    }

    @PatchMapping("/tasks/{taskId}/status")
    public TaskResponse updateStatus(@RequestHeader("X-Auth-Token") String token,
                                     @PathVariable Long taskId,
                                     @Valid @RequestBody UpdateTaskStatusRequest request) {
        return taskService.updateStatus(authService.requireUser(token), taskId, request.status());
    }

    @GetMapping("/projects/{projectId}/progress")
    public ProjectProgressResponse progress(@RequestHeader("X-Auth-Token") String token,
                                            @PathVariable Long projectId) {
        return taskService.progress(authService.requireUser(token), projectId);
=======
import com.itcompany.pms.dto.TaskDTO;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.service.TaskService;
import com.itcompany.pms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/api/projects/{projectId}/tasks")
    public List<TaskDTO> byProject(Principal principal, @PathVariable Long projectId) {
        User currentUser = userService.findByUsername(principal.getName());
        return taskService.findByProject(currentUser, projectId);
    }

    @PostMapping("/api/projects/{projectId}/tasks")
    public TaskDTO create(Principal principal, @PathVariable Long projectId, @Valid @RequestBody TaskDTO dto) {
        User currentUser = userService.findByUsername(principal.getName());
        return taskService.create(currentUser, projectId, dto);
    }

    @GetMapping("/api/tasks/{taskId}")
    public TaskDTO get(Principal principal, @PathVariable Long taskId) {
        User currentUser = userService.findByUsername(principal.getName());
        return taskService.findById(currentUser, taskId);
    }

    @PutMapping("/api/tasks/{taskId}")
    public TaskDTO update(Principal principal, @PathVariable Long taskId, @Valid @RequestBody TaskDTO dto) {
        User currentUser = userService.findByUsername(principal.getName());
        return taskService.update(currentUser, taskId, dto);
    }

    @PatchMapping("/api/tasks/{taskId}/status")
    public TaskDTO updateStatus(Principal principal, @PathVariable Long taskId, @Valid @RequestBody TaskDTO dto) {
        User currentUser = userService.findByUsername(principal.getName());
        return taskService.updateStatus(currentUser, taskId, dto);
>>>>>>> origin/main
    }
}
