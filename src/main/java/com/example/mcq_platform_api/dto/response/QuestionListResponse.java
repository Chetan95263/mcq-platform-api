package com.example.mcq_platform_api.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionListResponse {
    private String sessionId;
    private int total;
    private String subject;
    private String topic;
    private List<QuestionResponse> questions;
}
