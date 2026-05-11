package com.example.mcq_platform_api.cache;
import org.springframework.stereotype.Component;

import com.example.mcq_platform_api.dto.response.AnswerResponse;
import com.example.mcq_platform_api.exception.ResourceNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnswerCache {


    private final Map<String,AnswerResponse> answerCache = new ConcurrentHashMap<>();

    public void cacheAnswer(AnswerResponse answer) {
        answerCache.put(answer.getQuestionId(), answer);
    }

    public AnswerResponse getAnswer(String questionId) {
        if(!answerCache.containsKey(questionId)) return null;
        return answerCache.get(questionId);
    }

    public void removeAnswer(String questionId) {
        if(!answerCache.containsKey(questionId)) throw new ResourceNotFoundException("Question not found");
        answerCache.remove(questionId);
    }
}
