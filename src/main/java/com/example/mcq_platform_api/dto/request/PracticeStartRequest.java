package com.example.mcq_platform_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PracticeStartRequest {
    private String subject;
    private String topic;
    private int limit;
    private String time;
}
