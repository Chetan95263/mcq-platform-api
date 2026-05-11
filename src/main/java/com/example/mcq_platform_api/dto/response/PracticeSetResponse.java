package com.example.mcq_platform_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PracticeSetResponse {

    private String time;
    private QuestionListResponse questionListResponse;

}
