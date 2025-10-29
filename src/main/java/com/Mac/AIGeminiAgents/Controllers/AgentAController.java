package com.Mac.AIGeminiAgents.Controllers;

import com.Mac.AIGeminiAgents.DTO.ChatRequest;
import com.Mac.AIGeminiAgents.Services.ChatService;
import com.Mac.AIGeminiAgents.Services.TechnicalDocsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tech")
public class AgentAController {

    private final ChatService chatService;

    public AgentAController(TechnicalDocsService docsService, ChatService chatService) {
        this.chatService = chatService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ask")
    public String ask(@RequestBody ChatRequest chatRequest) {
        String userQuestion = chatRequest.getUserQuestion();
        String clarificationAnswer = chatRequest.getClarificationAnswer();

        String sessionHistory = chatRequest.getSessionHistory();
        return chatService.processQuestion(userQuestion, clarificationAnswer,sessionHistory);
    }
}
