package com.itcompany.pms.controller;

<<<<<<< HEAD
import com.itcompany.pms.dto.CreateUserRequest;
import com.itcompany.pms.dto.UpdateUserRoleRequest;
import com.itcompany.pms.dto.UserResponse;
import com.itcompany.pms.service.AuthService;
=======
import com.itcompany.pms.dto.UserDTO;
import com.itcompany.pms.entity.User;
>>>>>>> origin/main
import com.itcompany.pms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
=======
import java.security.Principal;
>>>>>>> origin/main
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

<<<<<<< HEAD
    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
=======
    private final UserService userService;

    public UserController(UserService userService) {
>>>>>>> origin/main
        this.userService = userService;
    }

    @GetMapping
<<<<<<< HEAD
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
=======
    public List<UserDTO> list(Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        return userService.findAll(currentUser);
    }

    @PostMapping
    public UserDTO create(Principal principal, @Valid @RequestBody UserDTO dto) {
        User currentUser = userService.findByUsername(principal.getName());
        return userService.create(currentUser, dto);
    }

    @PutMapping("/{id}")
    public UserDTO update(Principal principal, @PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        User currentUser = userService.findByUsername(principal.getName());
        return userService.update(currentUser, id, dto);
>>>>>>> origin/main
    }
}
