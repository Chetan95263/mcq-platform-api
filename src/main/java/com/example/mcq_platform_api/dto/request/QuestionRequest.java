package com.example.mcq_platform_api.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequest {
    private String subject;
    private String topic;
    private String questionText;
    private List<OptionRequest> options;
    private String explanation;
}
