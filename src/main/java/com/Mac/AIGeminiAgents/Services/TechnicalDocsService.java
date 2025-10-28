package com.Mac.AIGeminiAgents.Services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.*;

@Service
public class TechnicalDocsService {
    private final Map<String, String> docs = new HashMap<>();
    private final Map<String, List<String>> aliases = new HashMap<>();


    private String readPdf(String path) {
        try (PDDocument document = PDDocument.load(Paths.get(path).toFile())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public TechnicalDocsService() {
        try {
            docs.put("Angular", readPdf("src/main/resources/docs/Angular_Framework_Guide.pdf"));
            docs.put("Decision Tree", readPdf("src/main/resources/docs/Decision_Tree_Model.pdf"));
            docs.put("ESP32", readPdf("src/main/resources/docs/ESP32_Guide.pdf"));
            docs.put("Network Monitoring system", readPdf("src/main/resources/docs/Network_Monitoring_System.pdf"));
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
                "esp32", "microcontroller", "wifi", "bluetooth", "iot", "arduino", "sensor", "firmware", "embedded","system"
        ));
        // Alias dla Network Monitoring system
        aliases.put("Network Monitoring system", Arrays.asList(
                "network", "monitoring", "system", "snmp", "traffic", "topology", "alert", "packet", "device", "server"
        ));
    }

    public List<String> findDocsByField(String field) {
        if (field == null || field.isEmpty()) {
            return Collections.emptyList();
        }
        for (Map.Entry<String, List<String>> entry : aliases.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(field)) {
                return entry.getValue();
            }
        }
        return Collections.emptyList();
    }

    public List<String> findDocsByMessageAliases(String message) {
        Set<String> tokens = new HashSet<>(Arrays.asList(message.toLowerCase().split("[^a-zA-Z0-9]+")));
        List<String> matchedTopics = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : aliases.entrySet()) {
            for (String alias : entry.getValue()) {
                if (tokens.contains(alias.toLowerCase())) {
                    matchedTopics.add(entry.getKey());
                    break;
                }
            }
        }
        return matchedTopics;
    }

    public String getDocContent(String field) {
        if (field == null || field.isEmpty()) {
            return null;
        }
        for (String key : docs.keySet()) {
            if (key.equalsIgnoreCase(field)) {
                return docs.get(key);
            }
        }
        return null;
    }

}
