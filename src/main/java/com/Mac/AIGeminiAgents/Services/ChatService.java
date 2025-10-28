package com.Mac.AIGeminiAgents.Services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final TechnicalDocsService docsService;

    public ChatService(ChatClient.Builder builder, TechnicalDocsService docsService) {
        this.chatClient = builder.build();
        this.docsService = docsService;
    }

    //Agent A
    public String processQuestion(String userQuestion, String clarificationAnswer) {
        String docs;
        if(clarificationAnswer != null && !clarificationAnswer.trim().isEmpty()){
            docs = docsService.getDocContent(clarificationAnswer);
            return agentA(userQuestion,docs);
        }
        List<String> fieldList = docsService.findDocsByMessageAliases(userQuestion);

        if(fieldList.size()==1){
            docs = docsService.getDocContent(fieldList.getFirst());
            return agentA(userQuestion,docs);
        }
        if(fieldList.size()>1){

            return "Czy Twoje pytanie dotyczy: " +
                    String.join(" / ", fieldList) +
                    "? Proszę doprecyzować, aby otrzymać bardziej szczegółową odpowiedź.";
        }
        return agentB("Say Hi in spanish");
    }

    public String agentA(String userQuestion, String docs){
        String prompt = "You are a technical support expert named AgentA - always introduce your name. Use ONLY the docs below to answer. Docs: " + docs;
        return chatClient.prompt()
                .system(prompt)
                .user(userQuestion)
                .call()
                .content();
    }
    public String agentB(String userQuestion){
        String prompt= "You are AgentB";
        return chatClient.prompt()
                .system(prompt)
                .user(userQuestion)
                .call()
                .content();
    }

}
