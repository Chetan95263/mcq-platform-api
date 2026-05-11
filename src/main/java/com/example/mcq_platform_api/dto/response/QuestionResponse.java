package com.example.mcq_platform_api.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponse {
    private int number;
    private String questionId;
    private String questionText;
    private List<OptionResponse> options;    
}
