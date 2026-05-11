package com.example.mcq_platform_api.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.mcq_platform_api.service.AttemptSessionService;

@Component
public class ValidateRequestCache {
    private final Map<String,AttemptSessionService> cache = new ConcurrentHashMap<>();
    private void cacheAttemptSessionService(String practiceSetId, AttemptSessionService attemptSessionService){
        cache.put(practiceSetId, attemptSessionService);
    }
    private AttemptSessionService getAttemptSessionService(String practiceSetId){
        return cache.get(practiceSetId);
    }
    public void removeAttemptSessionService(String practiceSetId){
        cache.remove(practiceSetId);
    }
    public void createSession(String practiceSetId , String time){
        AttemptSessionService attemptSessionService = new AttemptSessionService();
        attemptSessionService.createAttemptSession(time);
        cacheAttemptSessionService(practiceSetId, attemptSessionService);
    }
    public  boolean isSessionActive(String practiceSetId){
        AttemptSessionService attemptSessionService = getAttemptSessionService(practiceSetId);
        if(attemptSessionService == null) return false;
        boolean isActive = attemptSessionService.isActive();
        if(!isActive) removeAttemptSessionService(practiceSetId);
        return isActive;
    }
}
