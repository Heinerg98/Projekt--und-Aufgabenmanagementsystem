package com.itcompany.pms.controller;

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
    }
}
