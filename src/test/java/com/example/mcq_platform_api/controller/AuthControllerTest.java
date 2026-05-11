package com.example.mcq_platform_api.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.mcq_platform_api.dto.request.LoginRequest;
import com.example.mcq_platform_api.entities.User;
import com.example.mcq_platform_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogin() throws Exception {
         LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("1234");

        User user = new User();
        user.setId("1");
        user.setUsername("john");
        user.setPassword("1234");
        Mockito.when(userService.findByUsername("john")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful for user: "))
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test 
    public void testSignup() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("1234");

        // User user = new User();
        // user.setId("1");
        // user.setUsername("john");
        // user.setPassword("1234");
        Mockito.when(userService.findByUsername("john")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signup successful for user: "))
                .andExpect(jsonPath("$.username").value("john"));
    }
}
