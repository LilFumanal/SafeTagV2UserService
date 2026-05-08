package com.lil.safetagv2userservice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String email;
    private String pseudo;
    private Role role;
}
