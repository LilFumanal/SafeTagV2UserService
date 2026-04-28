package com.lil.safetagv2userservice.service;

import com.lil.safetagv2userservice.entity.User;
import com.lil.safetagv2userservice.models.UserResponse;
import com.lil.safetagv2userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // éviter le problème du @PostConstruct
        ReflectionTestUtils.setField(userService, "colors", List.of("blue"));
    }

    @Test
    void shouldRegisterUser() {

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(userRepository.existsByPseudo(anyString())).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail("test@mail.com");
        savedUser.setPseudo("blue");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.register("test@mail.com", "password");

        assertEquals("test@mail.com", response.getEmail());
        assertEquals("blue", response.getPseudo());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowIfEmailAlreadyUsed() {

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.register("test@mail.com", "password")
        );

        assertEquals("Email already used", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldGetUserById() {

        UUID id = UUID.randomUUID();

        User user = new User();
        user.setId(id);
        user.setEmail("test@mail.com");
        user.setPseudo("blue");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(id);

        assertEquals(id, response.getId());
        assertEquals("test@mail.com", response.getEmail());
    }

    @Test
    void shouldThrowIfUserNotFound() {

        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.getUserById(id)
        );

        assertEquals("User not found", exception.getMessage());
    }
}
