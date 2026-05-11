package com.example.mcq_platform_api.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import com.example.mcq_platform_api.dto.request.AnswerRequest;
import com.example.mcq_platform_api.dto.request.PracticeSetSubmitRequest;

@Component
public class UserAnswerSubmitCache {
    private final Map<String,PracticeSetSubmitRequest> cache = new ConcurrentHashMap<>();
    public void saveUserAnswers(PracticeSetSubmitRequest request, String practiceSetId){ 
        if(!cache.containsKey(practiceSetId)) cache.put(practiceSetId, request);
        else {
            PracticeSetSubmitRequest existingRequest = cache.get(practiceSetId);
            List<AnswerRequest> answers = request.getAnswers();
            mergeLists(existingRequest.getAnswers(), answers);
            cache.put(practiceSetId, existingRequest);
        }
    }
    public PracticeSetSubmitRequest getUserAnswer(String practiceSetId) {
        if(!cache.containsKey(practiceSetId)) return null;
        return cache.get(practiceSetId);
    }
    public void removeUserAnswer(String practiceSetId) {
        cache.remove(practiceSetId);
    }
    private static void mergeLists(List<AnswerRequest> oldList, List<AnswerRequest> newList) {
        Map<String, AnswerRequest> oldMap = new HashMap<>();
        for (AnswerRequest answer : oldList) {
            oldMap.put(answer.getQuestionId(), answer);
        }

        for (AnswerRequest answer : newList) {
            if (oldMap.containsKey(answer.getQuestionId())) {
                AnswerRequest oldItem = oldMap.get(answer.getQuestionId());
                oldItem.setSelectedOption(answer.getSelectedOption()); 
            } else {
                oldList.add(answer);
            }
        }
    }
}
