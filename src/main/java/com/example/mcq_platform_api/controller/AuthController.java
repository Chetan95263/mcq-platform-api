package com.example.mcq_platform_api.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mcq_platform_api.auth.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if(loginRequest.getUsername() == null || loginRequest.getPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Username and password are required", null));
        }
       try{
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        
        String token = jwtUtil.generateToken(auth.getName());
        return ResponseEntity.ok(Map.of(
            "token",token,
            "message" , "Login successful",
            "Username" , loginRequest.getUsername()
        )
        );
    }catch(BadCredentialsException e){
        return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password", null));
    }

    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest) {
        if(signupRequest.getUsername() == null || signupRequest.getPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Username and password are required", null));
        }
        var existingUser = userService.findByUsername(signupRequest.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("Username already taken", null));
        }
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setId("USER");
        userService.saveUser(user);
        return ResponseEntity.ok(new AuthResponse("Signup successful for user: ", user.getUsername()));
    }
}
