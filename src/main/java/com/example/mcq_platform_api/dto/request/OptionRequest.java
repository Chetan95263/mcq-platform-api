package com.example.mcq_platform_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionRequest {
    private String optionText;
    private boolean correct;
}
