package com.example.mcq_platform_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequest {
    private String questionId;
    private char selectedOption;
}
