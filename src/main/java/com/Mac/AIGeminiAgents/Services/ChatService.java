package com.Mac.AIGeminiAgents.Services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ChatService {

    private final TechnicalDocsService docsService;
    private final AgentAService agentAService;
    private final AgentBService agentBService;

    public ChatService(AgentAService agentAService, AgentBService agentBService, TechnicalDocsService docsService) {
        this.docsService = docsService;
        this.agentAService = agentAService;
        this.agentBService = agentBService;
    }


    public String processQuestion(String userQuestion, String clarificationAnswer,String sessionHistory) {

        List<String> billingKeywords = Arrays.asList(
                "refund", "billing", "invoice", "payment", "charge", "price", "plan"
        );

        String lowerQuestion = userQuestion.toLowerCase();
        boolean isBillingQuestion = billingKeywords.stream().anyMatch(lowerQuestion::contains);

        if (isBillingQuestion) {
            return agentBService.agentB(userQuestion, sessionHistory);
        }

        if (clarificationAnswer != null && !clarificationAnswer.trim().isEmpty()) {
            String docs = docsService.getDocContent(clarificationAnswer);
            return agentAService.agentA(userQuestion, docs, sessionHistory);
        }

        List<String> fieldList = docsService.findDocsByMessageAliases(userQuestion);

        if (fieldList.size() == 1) {
            String docs = docsService.getDocContent(fieldList.get(0));
            return agentAService.agentA(userQuestion, docs, sessionHistory);
        }

        if (fieldList.size() > 1) {
            return "Is your question related to: " +
                    String.join(" or ", fieldList) +
                    "? Please answer.";
        }

        return agentBService.agentB(userQuestion, sessionHistory);
    }







}
