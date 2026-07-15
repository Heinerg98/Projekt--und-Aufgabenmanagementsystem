package com.itcompany.pms.controller;

import com.itcompany.pms.dto.UserDTO;
import com.itcompany.pms.entity.User;
import com.itcompany.pms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
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
    }
}
