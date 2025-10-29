package com.Mac.AIGeminiAgents.Services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AgentAService {
    private final ChatClient chatClient;

    public AgentAService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String agentA(String userQuestion, String docs, String sessionHistory) {
        String prompt = "You are a technical support expert named AgentA - always introduce your name. "+
                "Use ONLY the docs below to answer and session history. " +sessionHistory+
                "Docs: " + docs;
        return chatClient.prompt()
                .system(prompt)
                .user(userQuestion)
                .call()
                .content();
    }
}
