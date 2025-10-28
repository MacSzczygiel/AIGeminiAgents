package com.Mac.AIGeminiAgents.Controllers;

import com.Mac.AIGeminiAgents.DTO.ChatRequest;
import com.Mac.AIGeminiAgents.Services.ChatService;
import com.Mac.AIGeminiAgents.Services.TechnicalDocsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tech")
public class AgentAController {

    private final ChatService chatService;

    public AgentAController(TechnicalDocsService docsService, ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public String ask(@RequestBody ChatRequest chatRequest) {
        String userQuestion = chatRequest.getUserQuestion();
        String clarificationAnswer = chatRequest.getClarificationAnswer();
        return chatService.processQuestion(userQuestion, clarificationAnswer);
    }
    @PostMapping("/ask2")
    public String ask2(@RequestBody ChatRequest chatRequest) {
        return chatService.agentB("Say Hi in spanish");
    }
}
