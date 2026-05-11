package com.example.mcq_platform_api.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.mcq_platform_api.dto.response.QuestionListResponse;
import com.example.mcq_platform_api.entities.Question;
import com.example.mcq_platform_api.exception.ResourceNotFoundException;

@Component
public class QuestionListCache {

    private final Map<String, List<Question>> questionListCache = new ConcurrentHashMap<>();
    private final Map<String , QuestionListResponse> questionListResponseCache = new ConcurrentHashMap<>();

    public void cacheQuestion(String sessionId , List<Question> questions) {
        questionListCache.put(sessionId, questions);
    }
    public void cacheQuestionListResponse(String sessionId , QuestionListResponse questionListResponse) {
        questionListResponseCache.put(sessionId, questionListResponse);
    }

    public QuestionListResponse getQuestionListResponse(String sessionId) {
        if(!questionListResponseCache.containsKey(sessionId)) return null;
        return questionListResponseCache.get(sessionId);
    }

    public List<Question> getQuestion(String sessionId) {
        if(!questionListCache.containsKey(sessionId)) return null;
        return questionListCache.get(sessionId);
    }

    public void removeQuestion(String sessionId) {
        if(!questionListCache.containsKey(sessionId)) throw new ResourceNotFoundException("Question not found");
        questionListCache.remove(sessionId);
    }
    public void removeQuestionListResponse(String sessionId) {
        if(!questionListResponseCache.containsKey(sessionId)) throw new ResourceNotFoundException("Question List Response not found");
        questionListResponseCache.remove(sessionId);
    }
}
