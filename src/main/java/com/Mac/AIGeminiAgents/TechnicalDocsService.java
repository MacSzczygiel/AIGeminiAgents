package com.Mac.AIGeminiAgents;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class TechnicalDocsService {
    private final Map<String, String> docs = new HashMap<>();
    private final Map<String, List<String>> aliases = new HashMap<>();



    public TechnicalDocsService() {
        try {
            docs.put("Angular", Files.readString(Paths.get("src/main/resources/docs/Angular_Framework_Guide.pdf")));
            docs.put("Decision Tree", Files.readString(Paths.get("src/main/resources/docs/Decision_Tree_Model.pdf")));
            docs.put("ESP32", Files.readString(Paths.get("src/main/resources/docs/ESP32_Guide.pdf")));
            docs.put("Network Monitoring system", Files.readString(Paths.get("src/main/resources/docs/Network_Monitoring_System.pdf")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Alias dla Angular
        aliases.put("Angular", Arrays.asList(
                "angular", "typescript", "spa", "google framework", "frontend", "component", "template", "module", "service", "routing", "cli"
        ));
        // Alias dla Decision Tree
        aliases.put("Decision Tree", Arrays.asList(
                "decision tree", "tree", "model", "ml", "machine learning", "classification", "regression", "split", "node"
        ));
        // Alias dla ESP32
        aliases.put("ESP32", Arrays.asList(
                "esp32", "microcontroller", "wifi", "bluetooth", "iot", "arduino", "sensor", "firmware", "embedded"
        ));
        // Alias dla Network Monitoring system
        aliases.put("Network Monitoring system", Arrays.asList(
                "network", "monitoring", "system", "snmp", "traffic", "topology", "alert", "packet", "device", "server"
        ));
    }

    public String findRelevantDocs(String message) {
        // Prosty przykład: wybierz dokumentację na podstawie słowa kluczowego z pytania
        for (Map.Entry<String, String> entry : docs.entrySet()) {
            if (message.toLowerCase().contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        // Jeśli nie znaleziono pasującego dokumentu
        return "Brak odpowiedniej dokumentacji do tego pytania.";
    }

    public String findRelevantDocsWithClarification(String message) {
        Set<String> tokens = new HashSet<>(Arrays.asList(message.toLowerCase().split("[^a-zA-Z0-9]+")));
        List<String> matchedTopics = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : aliases.entrySet()) {
            for (String alias : entry.getValue()) {
                if (tokens.contains(alias)) {
                    matchedTopics.add(entry.getKey());
                    break;
                }
            }
        }
        if (matchedTopics.isEmpty()) {
            return "Brak odpowiedniej dokumentacji do tego pytania.";
        }

        if (matchedTopics.size() == 1) {
            return docs.get(matchedTopics.get(0)); // standardowy zwrot
        }

        // Użytkownik doprecyzowuje temat
        String clarificationPrompt = "Czy Twoje pytanie dotyczy: " +
                String.join(" / ", matchedTopics) +
                "? Proszę doprecyzować, aby otrzymać bardziej szczegółową odpowiedź.";

        return clarificationPrompt;
    }



}
