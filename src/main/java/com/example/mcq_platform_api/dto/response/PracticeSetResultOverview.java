package com.example.mcq_platform_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeSetResultOverview {
    private String status;
    private String questionText;
    private char correctOption;
    private String explanation;
    private String correctOptionText;
}
