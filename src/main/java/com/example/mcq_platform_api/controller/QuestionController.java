package com.example.mcq_platform_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mcq_platform_api.appconstants.Constant;
import com.example.mcq_platform_api.cache.AnswerCache;
import com.example.mcq_platform_api.cache.AnswerListCache;
import com.example.mcq_platform_api.dto.response.AnswerListResponse;
import com.example.mcq_platform_api.dto.response.AnswerResponse;
import com.example.mcq_platform_api.dto.response.QuestionListResponse;
import com.example.mcq_platform_api.dto.response.QuestionResponse;
import com.example.mcq_platform_api.exception.ResourceNotFoundException;
import com.example.mcq_platform_api.service.QuestionService;


@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerCache answerCacheService;
    @Autowired 
    private AnswerListCache tempService;
   
    @GetMapping("/questions")
    public ResponseEntity<QuestionListResponse> getQuestions(@RequestParam(required = false) String subject ,
        @RequestParam(required = false) String topic , @RequestParam(defaultValue = Constant.DEFAULT_QUESTION_LIMIT) int limit) {
        if(subject != null){
            subject = subject.toLowerCase();
        }
        if(topic != null){
            topic = topic.toLowerCase();
        }
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
