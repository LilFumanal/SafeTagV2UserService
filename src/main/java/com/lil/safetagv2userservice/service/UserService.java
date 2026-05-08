package com.lil.safetagv2userservice.service;

import com.lil.safetagv2userservice.auth.JwtService;
import com.lil.safetagv2userservice.entity.User;
import com.lil.safetagv2userservice.models.UserResponse;
import com.lil.safetagv2userservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already used");
        }
        String pseudo;
        do {
            pseudo = generatePseudo();
        } while (userRepository.existsByPseudo(pseudo));

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());

        user.setPseudo(pseudo);

        return mapToResponse(userRepository.save(user));
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setPseudo(user.getPseudo());
        return response;
    }

    private List<String> colors;

    @PostConstruct
    public void loadPseudos() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("colors.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            colors = reader.lines().toList();
        }
    }

    private String generatePseudo() {
        if (colors == null || colors.isEmpty()) {
            throw new RuntimeException("Pseudo list not loaded");
        }
        return colors.get(new Random().nextInt(colors.size()));
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Identifiants invalides"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Identifiants invalides");
        }
        return jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());
    }

}
