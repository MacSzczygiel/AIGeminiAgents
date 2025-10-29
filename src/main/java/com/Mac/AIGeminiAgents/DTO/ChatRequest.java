package com.Mac.AIGeminiAgents.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ChatRequest {
    private String userQuestion;
    private String clarificationAnswer;
    private String sessionHistory;

}
