package com.itcompany.pms.controller;

import com.itcompany.pms.dto.CreateUserRequest;
import com.itcompany.pms.dto.UpdateUserRoleRequest;
import com.itcompany.pms.dto.UserResponse;
import com.itcompany.pms.service.AuthService;
import com.itcompany.pms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> listUsers(@RequestHeader("X-Auth-Token") String token) {
        return userService.listUsers(authService.requireUser(token));
    }

    @PostMapping
    public UserResponse createUser(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(authService.requireUser(token), request);
    }

    @PatchMapping("/{id}/role")
    public UserResponse updateRole(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id, @Valid @RequestBody UpdateUserRoleRequest request) {
        return userService.updateRole(authService.requireUser(token), id, request.role());
    }
}
