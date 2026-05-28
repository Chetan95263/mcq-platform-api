package com.example.mcq_platform_api.service;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mcq_platform_api.auth.JwtUtil;
import com.example.mcq_platform_api.entities.User;
import com.example.mcq_platform_api.exception.ConflictException;
import com.example.mcq_platform_api.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(String username, String password){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        return jwtUtil.generateToken(username);
    }
    public void register(String username, String password) {
        var existingUser = userRepo.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new ConflictException("Username already taken");
        }
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepo.save(user);
    }
    
}
