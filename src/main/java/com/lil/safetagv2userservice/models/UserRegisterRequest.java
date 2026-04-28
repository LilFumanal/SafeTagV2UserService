package com.lil.safetagv2userservice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

    private String email;
    private String password;
}
