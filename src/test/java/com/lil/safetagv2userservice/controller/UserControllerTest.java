package com.lil.safetagv2userservice.controller;

import com.lil.safetagv2userservice.models.UserResponse;
import com.lil.safetagv2userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Utilisation de MockitoBean pour ta version
    private UserService userService;

    @Test
    void shouldRegisterUser() throws Exception {
        UserResponse response = new UserResponse();
        response.setId(UUID.randomUUID());
        response.setEmail("test@mail.com");
        response.setPseudo("test");

        when(userService.register(anyString(), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()) // Nécessaire avec Security
                        .content("""
                        {
                          "email": "test@mail.com",
                          "password": "password"
                        }
                    """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUserById() throws Exception {

        UUID userId = UUID.randomUUID();

        UserResponse response = new UserResponse();
        response.setId(userId);
        response.setEmail("test@mail.com");
        response.setPseudo("test");

        when(userService.getUserById(userId))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }

    @Test
    void shouldReturn400WhenEmailAlreadyUsed() throws Exception {

        when(userService.register(anyString(), anyString()))
                .thenThrow(new RuntimeException("Email already used"));

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "email": "test@mail.com",
                            "password": "password"
                        }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already used"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {

        UUID id = UUID.randomUUID();

        when(userService.getUserById(id))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/v1/users/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

}
