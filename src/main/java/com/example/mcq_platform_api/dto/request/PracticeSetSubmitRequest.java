package com.example.mcq_platform_api.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeSetSubmitRequest {
    private List<AnswerRequest> answers;
}
