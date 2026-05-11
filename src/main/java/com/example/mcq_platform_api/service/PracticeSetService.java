package com.example.mcq_platform_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.mcq_platform_api.cache.AnswerListCache;
import com.example.mcq_platform_api.cache.PracticeSetCache;
import com.example.mcq_platform_api.cache.QuestionListCache;
import com.example.mcq_platform_api.cache.UserAnswerSubmitCache;
import com.example.mcq_platform_api.cache.ValidateRequestCache;
import com.example.mcq_platform_api.dto.request.AnswerRequest;
import com.example.mcq_platform_api.dto.request.PracticeSetSubmitRequest;
import com.example.mcq_platform_api.dto.request.PracticeStartRequest;
import com.example.mcq_platform_api.dto.response.AnswerResponse;

import com.example.mcq_platform_api.dto.response.PracticeSetResponse;
import com.example.mcq_platform_api.dto.response.PracticeSetResultOverview;
import com.example.mcq_platform_api.dto.response.PracticeSetResultResponse;
import com.example.mcq_platform_api.dto.response.QuestionListResponse;

import com.example.mcq_platform_api.entities.PracticeSet;
import com.example.mcq_platform_api.entities.PracticeSetItem;
import com.example.mcq_platform_api.entities.Question;
import com.example.mcq_platform_api.exception.BadRequestException;
import com.example.mcq_platform_api.exception.ResourceNotFoundException;
import com.example.mcq_platform_api.repository.PracticeSetRepo;

@Service
public class PracticeSetService {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerListCache answerListCacheService;

    @Autowired
    private ValidateRequestCache validateRequestCache;

    @Autowired
    private UserAnswerSubmitCache userAnswerSubmitCache;

    @Autowired
    private QuestionListCache questionListCacheService;

    @Autowired
    private PracticeSetRepo practiceSetRepo;

    @Autowired
    private PracticeSetCache practiceSetCache;
    
    public PracticeSetResponse generatePracticeSet(PracticeStartRequest request) {
        String time = (request != null) ? request.getTime() : "";
        int limit = 5;
        String subject = null;
        String topic = null;
        if(request != null) limit = request.getLimit() == 0 ? 5 : request.getLimit();
        if(time == null || time == "") time = "10";
         if(request != null && request.getSubject() != null){
            subject = request.getSubject().toLowerCase();
        }
        if(request != null && request.getTopic() != null){
            topic = request.getTopic().toLowerCase();
        }
        QuestionListResponse questionListResponse = questionService.getQuestions(subject, subject, limit);
        validateRequestCache.createSession(questionListResponse.getSessionId(), time);
        PracticeSetResponse practiceSetResponse = new PracticeSetResponse(time , questionListResponse);
        practiceSetCache.cachePracticeSet(questionListResponse.getSessionId(), practiceSetResponse);
        return practiceSetResponse;
    }
    public int submitPracticeSet(String practiceSetId, PracticeSetSubmitRequest request) {
        if(request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new BadRequestException("No answers provided in the request");
        }
        if(!validateRequestCache.isSessionActive(practiceSetId)) {
            throw new ResourceNotFoundException("Practice set session has expired");
        }
        userAnswerSubmitCache
        .saveUserAnswers(request, practiceSetId);
        return request.getAnswers().size();
    }
  
    public PracticeSetResponse getSavedPracticeSet(String practiceSetId , PracticeStartRequest request, Authentication authentication) {
        PracticeSet practiceSet = practiceSetRepo.findByIdAndUserId(practiceSetId , "Aman")
        .orElseThrow(() -> new ResourceNotFoundException("Practice set not found"));
        return practiceSetToPracticeSetResponse(practiceSet , request);
        
    }
    public void savePracticeSet(String practiceSetId) {
        List<Question> questionList = questionListCacheService.getQuestion(practiceSetId);
        QuestionListResponse questionListResponse = questionListCacheService.getQuestionListResponse(practiceSetId);
        if(questionList == null) {
            throw new ResourceNotFoundException("Practice set session not found");
        }
        System.out.println(questionList.get(0).getQuestionText());
        PracticeSet practiceSet = new PracticeSet();
        practiceSet.setId(practiceSetId);
        
        practiceSet.setUserId("Aman");

        practiceSet.setSubject(questionListResponse.getSubject());
        practiceSet.setTopic(questionListResponse.getTopic());
        practiceSet.setDateAndTime(LocalDateTime.now().toString());
        
        PracticeSetItem practiceSetItem = new PracticeSetItem();
        practiceSetItem.setId(UUID.randomUUID().toString());
        practiceSetItem.setPracticeSet(practiceSet);
        practiceSetItem.setQuestions(questionList);

        practiceSet.setPracticeSetItems(practiceSetItem);
        
        var obj = practiceSetRepo.save(practiceSet);
        if(obj == null) {
            throw new RuntimeException("Failed to save practice set");
        }
    
    }
    public PracticeSetResultResponse getResult(String practiceSetId) {
         if(!validateRequestCache.isSessionActive(practiceSetId)) {
            throw new ResourceNotFoundException("Practice set session has expired");
         }
         List<AnswerRequest> userAnswers = userAnswerSubmitCache.getUserAnswer(practiceSetId).getAnswers();
         List<AnswerResponse> correctAnswer = answerListCacheService.getSession(practiceSetId).getAnswers();
         List<Question> questionList = questionListCacheService.getQuestion(practiceSetId);
         int total = correctAnswer.size();
         int marks = 0;
         Map<String , AnswerRequest> map = new HashMap<>();
         Map<String , Question> map2 = new HashMap<>();
        if(userAnswers != null) { 
            for(AnswerRequest ar : userAnswers) {
                map.put(ar.getQuestionId(),ar);
            }
        }
         for(Question question : questionList) {
            map2.put(question.getId() , question);
         }
         List<PracticeSetResultOverview> practiceSetResultOverviews = new ArrayList<>();
         
         for(AnswerResponse correct : correctAnswer) {
            PracticeSetResultOverview p = new PracticeSetResultOverview();
            p.setQuestionText(map2.get(correct.getQuestionId()).getQuestionText());
            p.setExplanation(map2.get(correct.getQuestionId()).getExplanation());
            p.setStatus("Not Answered");   
            p.setCorrectOptionText(correct.getCorrectOptionText());
            p.setCorrectOption(correct.getCorrectOption());
            if(userAnswers != null) {
                if(map.containsKey(correct.getQuestionId())){
                    if(correct.getCorrectOption() == map.get(correct.getQuestionId()).getSelectedOption()){
                        marks++;
                        p.setStatus("Correct Answer");
                    } 
                    else p.setStatus("Wrong Answer");
                } 
            }
            practiceSetResultOverviews.add(p);
         }
         validateRequestCache.removeAttemptSessionService(practiceSetId);
         questionListCacheService.removeQuestionListResponse(practiceSetId);
         questionListCacheService.removeQuestion(practiceSetId);
         userAnswerSubmitCache.removeUserAnswer(practiceSetId);
         answerListCacheService.removeSession(practiceSetId);

         PracticeSetResultResponse practiceSetResultResponse = new PracticeSetResultResponse();
         practiceSetResultResponse.setScore(marks+"/"+total);
         practiceSetResultResponse.setPracticeSetResultOverview(practiceSetResultOverviews);
         return practiceSetResultResponse;
    }
    
    private  PracticeSetResponse practiceSetToPracticeSetResponse(PracticeSet practiceSet , PracticeStartRequest request){
         QuestionListResponse questionListResponse = questionService.mapToQuestionListResponse(practiceSet.getPracticeSetItems().getQuestions(), practiceSet.getSubject(), practiceSet.getTopic());
        String time = request.getTime();
        if(time == "" || time == null) time = "10";
        validateRequestCache.createSession(questionListResponse.getSessionId(), time);
        PracticeSetResponse practiceSetResponse = new PracticeSetResponse(time , questionListResponse);
        practiceSetCache.cachePracticeSet(questionListResponse.getSessionId(), practiceSetResponse);
        return practiceSetResponse;
    }
    
}
