package com.example.mcq_platform_api.cache;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.mcq_platform_api.dto.response.AnswerListResponse;
import com.example.mcq_platform_api.dto.response.AnswerResponse;

@Component
public class AnswerListCache {
    private final Map<String,AnswerListResponse> sessionStore = new ConcurrentHashMap<>();
    public String createSession(List<AnswerResponse> answers){
        String sessionId = UUID.randomUUID().toString();
        AnswerListResponse tempSessionDTO = new AnswerListResponse(answers);
        sessionStore.put(sessionId, tempSessionDTO);
        return sessionId;
    }
    public AnswerListResponse getSession(String sessionId){
        return sessionStore.get(sessionId);
    }
    public void removeSession(String sessionId){
        sessionStore.remove(sessionId);
    }
}
