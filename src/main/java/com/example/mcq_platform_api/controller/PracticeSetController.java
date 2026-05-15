package com.example.mcq_platform_api.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mcq_platform_api.dto.request.PracticeSetSubmitRequest;
import com.example.mcq_platform_api.dto.request.PracticeStartRequest;
import com.example.mcq_platform_api.dto.response.MessageResponse;
import com.example.mcq_platform_api.dto.response.PracticeSetResponse;
import com.example.mcq_platform_api.dto.response.PracticeSetResultResponse;
import com.example.mcq_platform_api.service.PracticeSetService;


@RestController
@RequestMapping("/practice-set")
public class PracticeSetController {

    @Autowired
    private PracticeSetService practiceSetService;


    @PostMapping("/start")
    public ResponseEntity<PracticeSetResponse> getPracticeSet(@RequestBody(required = false) PracticeStartRequest request) {
        PracticeSetResponse response = practiceSetService.generatePracticeSet(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{practiceSetId}/start")
    public ResponseEntity<PracticeSetResponse> startPracticeSet(@PathVariable String practiceSetId , @RequestBody PracticeStartRequest request , Authentication authentication) {
        PracticeSetResponse response = practiceSetService.getSavedPracticeSet(practiceSetId, request , authentication );
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{practiceSetId}/submit")
    public ResponseEntity<MessageResponse> submitPracticeSet(@PathVariable String practiceSetId, @RequestBody PracticeSetSubmitRequest request) {
        int size = practiceSetService.submitPracticeSet(practiceSetId, request);
        return ResponseEntity.ok(new MessageResponse(size + " answers submitted successfully"));
    }

    @PostMapping("/{practiceSetId}/save")
    public ResponseEntity<MessageResponse> savePracticeSet(@PathVariable String practiceSetId) {
        practiceSetService.savePracticeSet(practiceSetId);
        
        return ResponseEntity.ok(new MessageResponse("Practice set saved successfully"));
    }
   @PostMapping("/{practiceSetId}/result")
    public ResponseEntity<PracticeSetResultResponse> getPracticeSetResult(@PathVariable String practiceSetId) {
        return ResponseEntity.ok(practiceSetService.getResult(practiceSetId));
    }
}
