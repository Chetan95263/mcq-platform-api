package com.example.mcq_platform_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mcq_platform_api.entities.PracticeSet;

public interface PracticeSetRepo extends JpaRepository<PracticeSet, String> {
    Optional<PracticeSet> findByIdAndUserId(String id, String userId);
}
