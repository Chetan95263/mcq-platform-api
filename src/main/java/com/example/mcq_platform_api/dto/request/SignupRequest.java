package com.example.mcq_platform_api.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class SignupRequest {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
