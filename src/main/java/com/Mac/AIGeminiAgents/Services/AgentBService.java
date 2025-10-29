package com.Mac.AIGeminiAgents.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AgentBService {
    private final ChatClient chatClient;

    public AgentBService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    private String extractJson(String text) {
        String cleaned = text.trim();

        if (cleaned.toLowerCase().startsWith("```json")) {
            cleaned = cleaned.substring(7).trim();
            if (cleaned.endsWith("```"))
                cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
        }
        return cleaned;
    }

    public String handleAgentResponse(String response, String sessionHistory, String userQuestion) {
        try {
            if (response == null || !response.contains("function_call")) {
                return response;
            }
            String cleanedResponse = extractJson(response);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(cleanedResponse);

            if (root.has("function_call")) {
                JsonNode funcCall = root.get("function_call");
                String functionName = funcCall.get("name").asText();
                JsonNode params = funcCall.get("parameters");

                switch (functionName) {
                    case "makeTicket":
                        return makeTicket(sessionHistory, userQuestion);
                    case "fillReturningForm":
                        String orderNumber = params.has("orderNumber") ? params.get("orderNumber").asText() : "";
                        String reason = params.has("reason") ? params.get("reason").asText() : "";
                        return fillReturningForm(orderNumber, reason);
                    default:
                        return "Unknow function: " + functionName;
                }
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknow text.";
        }
    }

    public String makeTicket(String sessionHistory, String userQuestion) {
        String filePath = "tickets_log.txt";  // możesz zmienić nazwę pliku
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        StringBuilder sb = new StringBuilder();
        sb.append("Timestamp: ").append(now).append("\n");
        sb.append("Session History:\n").append(sessionHistory).append("\n");
        sb.append("User Question:\n").append(userQuestion).append("\n");
        sb.append("--------------------------------------------------\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during making ticket.";
        }

        return "Thank you! Ticket was created.";
    }

    public String fillReturningForm(String orderNumber, String reason) {
        if (orderNumber == null || orderNumber.isEmpty() || reason == null || reason.isEmpty()) {
            return "No data to fill the form.";
        }

        String filePath = "return_forms_log.txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        StringBuilder sb = new StringBuilder();
        sb.append("Timestamp: ").append(now).append("\n");
        sb.append("Order Number: ").append(orderNumber).append("\n");
        sb.append("Reason: ").append(reason).append("\n");
        sb.append("--------------------------------------------------\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while filling the form.";
        }

        return "Form with order number " + orderNumber + " was sent for the reason: " + reason;
    }

    public String agentB(String userQuestion, String sessionHistory) {
        String prompt = "You are AgentB, a Billing Specialist.\n" +
                "\n" +
                "Your role is to handle basic billing requests with these capabilities:\n" +
                "\n" +
                "1. Fill return form:\n" +
                "   - Call function \"fillReturningForm\" with parameters:\n" +
                "     {\n" +
                "       \"orderNumber\": \"order number\",\n" +
                "       \"reason\": \"reason\"\n" +
                "     }\n" +
                "   - Only call this function if you have both orderNumber and reason. If you don't have both parameters ask for all info and don;t call any json function.\n" +
                "   - When calling, respond ONLY with the JSON object:\n" +
                "     {\n" +
                "       \"function_call\": {\n" +
                "         \"name\": \"fillReturningForm\",\n" +
                "         \"parameters\": {\n" +
                "           \"orderNumber\": \"...\",\n" +
                "           \"reason\": \"...\"\n" +
                "         }\n" +
                "       }\n" +
                "     }\n" +
                "\n" +
                "2. Confirm customer plans and pricing, explain refund policies and timelines clearly.\n" +
                "lava cake : 100;\n" +
                "normal cake : 125;\n" +
                "bread : 5;\n" +
                "buns : 1,20;\n" +
                "\n" +
                "prices in euro and gold customers have 10% less"+
                "\n" +
                "3. Make support ticket or make a report:\n" +
                "   - Call function \"makeTicket\" without parameters.\n" +
                "   - When calling, respond ONLY with the JSON object:\n" +
                "     {\n" +
                "       \"function_call\": {\n" +
                "         \"name\": \"makeTicket\",\n" +
                "         \"parameters\": {}\n" +
                "       }\n" +
                "     }\n" +
                "\n" +
                "When you call a function, respond with the pure JSON object only. Do not include any backticks or extraneous text." +
                "If a question is outside your expertise, politely indicate so and suggest forwarding to another agent or support. Maintain a helpful and professional tone.\n" +
                "\n" +
                "Use ONLY the docs below and the session history to answer.\n" +
                "\n" +
                "Session history:\n" + sessionHistory;

        String response = chatClient.prompt()
                .system(prompt)
                .user(userQuestion)
                .call()
                .content();
//        return response;
        return handleAgentResponse(response, sessionHistory, userQuestion);
    }
}
