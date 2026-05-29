package com.example.mcq_platform_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mcq_platform_api.appconstants.Constant;
import com.example.mcq_platform_api.cache.AnswerCache;
import com.example.mcq_platform_api.cache.AnswerListCache;
import com.example.mcq_platform_api.dto.response.AnswerListResponse;
import com.example.mcq_platform_api.dto.response.AnswerResponse;
import com.example.mcq_platform_api.dto.response.QuestionListResponse;
import com.example.mcq_platform_api.dto.response.QuestionResponse;
import com.example.mcq_platform_api.exception.ResourceNotFoundException;
import com.example.mcq_platform_api.service.QuestionService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class QuestionController {
    
    private final QuestionService questionService;
    private final AnswerCache answerCacheService;
    private final AnswerListCache tempService;
   
    @GetMapping("/questions")
    public ResponseEntity<QuestionListResponse> getQuestions(@RequestParam(required = false) String subject ,
        @RequestParam(required = false) String topic , @RequestParam(defaultValue = Constant.DEFAULT_QUESTION_LIMIT) int limit) {
      
        QuestionListResponse response = questionService.getQuestions(subject, topic, limit);
        return ResponseEntity.ok(response);

    }
    @GetMapping("/question/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable String id) {

        QuestionResponse response = questionService.getQuestionById(id);   
        return ResponseEntity.ok(response);
    }
    @GetMapping("/question/{id}/answer")
    public ResponseEntity<AnswerResponse> getAnswer(@PathVariable String id){
        AnswerResponse answerResponse = answerCacheService.getAnswer(id);
        if(answerResponse == null) answerResponse = questionService.getAnswerByQuestionId(id);
        return ResponseEntity.ok(answerResponse);
    }
    @GetMapping("/questions/{seesionId}/answer")
    public ResponseEntity<AnswerListResponse> getAnswers(@PathVariable String seesionId){
        AnswerListResponse tempSessionResponse = tempService.getSession(seesionId);
        if(tempSessionResponse == null) throw new ResourceNotFoundException("Session not Exists id:"+seesionId);
        return ResponseEntity.ok(tempSessionResponse);
    }
}
