package com.Mac.AIGeminiAgents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tech")
public class AgentAController {
    private final ChatClient chatClient;
    private final TechnicalDocsService docsService;


    public AgentAController(ChatClient.Builder builder, TechnicalDocsService docsService) {
        this.chatClient = builder.build();
        this.docsService = docsService;
    }

    @PostMapping("/ask")
    public String ask(@RequestBody String userQuestion) {
        String docs = docsService.findRelevantDocs(userQuestion);
        String prompt = "You are a technical support expert. Use ONLY the docs below to answer. If the docs do not cover it, say so. Docs: " + docs;
        return chatClient
                .prompt()
                .system(prompt)
                .user(userQuestion)
                .call()
                .content();
    }

}
