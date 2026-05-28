package com.example.mcq_platform_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mcq_platform_api.dto.request.LoginRequest;
import com.example.mcq_platform_api.dto.request.SignupRequest;
import com.example.mcq_platform_api.dto.response.AuthResponse;
import com.example.mcq_platform_api.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {    
    private final AuthService authService;    

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok( AuthResponse.builder()
                .message("Login successful")
                .token(token)
                .build());
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.register(signupRequest.getUsername(), signupRequest.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body( AuthResponse.builder()
                .message("Signup successful")
                .username(signupRequest.getUsername())
                .build());
    }
}
