package com.example.mcq_platform_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnswerResponse {
    private String questionId;
    private char correctOption;
    private String correctOptionText;
}
