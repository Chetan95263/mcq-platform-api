package com.example.mcq_platform_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mcq_platform_api.dto.request.QuestionRequest;
import com.example.mcq_platform_api.dto.request.QuestionUpdateRequest;
import com.example.mcq_platform_api.dto.response.MessageResponse;
import com.example.mcq_platform_api.exception.BadRequestException;
import com.example.mcq_platform_api.service.QuestionService;

@RestController
@RequestMapping("/admin/question")
public class AdminController {
    
    @Autowired
    private QuestionService questionService;

    @PostMapping
    public ResponseEntity<MessageResponse> addQuestion(@RequestBody List<QuestionRequest> questionRequests){
        int size = questionService.saveAllQuestion(questionRequests);
        if(size == 0) throw new BadRequestException("Error Occured");
        return ResponseEntity.ok(new MessageResponse(size+" Question Added Successfully!"));
    }
    @PutMapping
    public ResponseEntity<MessageResponse> updateQuestion(@RequestBody List<QuestionUpdateRequest> questionUpdateRequests){
        questionService.updateQuestion(questionUpdateRequests);
        return ResponseEntity.ok(new MessageResponse("Question Updated Successfully!"));
    }
    @DeleteMapping("/{questionId}")
    public ResponseEntity<MessageResponse> deleteQuestion(@PathVariable String questionId){
        questionService.deleteQuestionByQuestionId(questionId);
        return ResponseEntity.ok(new MessageResponse("Question Deleted Successfully!"));

    }
}
