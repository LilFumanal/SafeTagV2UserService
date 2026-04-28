package com.lil.safetagv2userservice.controller;

import com.lil.safetagv2userservice.models.LoginRequest;
import com.lil.safetagv2userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        // Appelle la méthode qu'on vient de créer
        String token = userService.login(request.email(), request.password());

        // Retourne le token avec un statut 200 OK
        return ResponseEntity.ok(token);
    }
}
