package com.lil.safetagv2userservice.controller;

import com.lil.safetagv2userservice.models.UserRegisterRequest;
import com.lil.safetagv2userservice.models.UserResponse;
import com.lil.safetagv2userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRegisterRequest request) {
        return userService.register(
                request.getEmail(),
                request.getPassword()
        );
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

}
