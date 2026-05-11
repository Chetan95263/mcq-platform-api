package com.example.mcq_platform_api.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
public class AttemptSessionService {
    private Instant startTime;
    private Instant endTime;
    private boolean isActive;
    public void createAttemptSession(String time){
        this.startTime = Instant.now();
        this.endTime = startTime.plus(Duration.ofMinutes(Long.parseLong(time)));
        this.isActive = true;
    }
    public boolean isActive(){
        if(!isActive) return false;
        if(Instant.now().isAfter(endTime)){
            isActive = false;
            return isActive;
        }
        return true;
    }
    
}
