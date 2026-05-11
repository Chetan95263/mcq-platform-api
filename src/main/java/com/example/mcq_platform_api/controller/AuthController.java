package com.example.mcq_platform_api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mcq_platform_api.dto.request.LoginRequest;
import com.example.mcq_platform_api.dto.request.SignupRequest;
import com.example.mcq_platform_api.dto.response.AuthResponse;
import com.example.mcq_platform_api.entities.User;
import com.example.mcq_platform_api.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired  
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = userService.findByUsername(loginRequest.getUsername());
        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid username or password", null));
        }
        return ResponseEntity.ok(new AuthResponse("Login successful for user: ", user.getUsername()));
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest) {
        var existingUser = userService.findByUsername(signupRequest.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("Username already taken", null));
        }
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(signupRequest.getPassword());
        userService.saveUser(user);
        return ResponseEntity.ok(new AuthResponse("Signup successful for user: ", user.getUsername()));
    }
}
