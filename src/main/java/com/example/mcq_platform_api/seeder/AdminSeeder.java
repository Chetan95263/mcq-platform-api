package com.example.mcq_platform_api.seeder;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.mcq_platform_api.entities.User;
import com.example.mcq_platform_api.repository.UserRepo;

@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if(userRepo.findByUsername("admin").isEmpty()) {

            User admin = new User();
            admin.setId(UUID.randomUUID().toString());

            admin.setUsername("admin");

            admin.setPassword(
                passwordEncoder.encode("admin123")
            );

            admin.setRole("ADMIN");

            userRepo.save(admin);
        }
    }
}
