package com.example.mcq_platform_api.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
