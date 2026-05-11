package com.example.mcq_platform_api.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnswerListResponse {
    private List<AnswerResponse> answers;
}
