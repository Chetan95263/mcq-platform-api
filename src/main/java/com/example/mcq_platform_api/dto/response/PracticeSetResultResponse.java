package com.example.mcq_platform_api.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeSetResultResponse {

    private String score;
    private List<PracticeSetResultOverview> PracticeSetResultOverview;
}
