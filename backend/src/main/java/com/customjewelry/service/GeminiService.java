package com.customjewelry.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String googleApiKey;
    @Value("${gemini.api.url}")
    private String googleUrl;

    @Value("${replicate.api.key}")
    private String replicateApiKey;
    @Value("${replicate.api.url}")
    private String replicateUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 1. TEXT GENERATION
    public String generateCreativeDescription(String prompt) {
        try {
            String finalUrl = googleUrl + "?key=" + googleApiKey;
            Map<String, Object> requestBody = new HashMap<>();
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> part = new HashMap<>();
            part.put("text", "Write a short, luxurious description (max 40 words) for: " + prompt);
            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(part));
            contents.add(content);
            requestBody.put("contents", contents);

            Map<String, Object> response = restTemplate.postForObject(finalUrl, requestBody, Map.class);
            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> candidate = candidates.get(0);
                    Map<String, Object> contentResponse = (Map<String, Object>) candidate.get("content");
                    List<Map<String, Object>> partsResponse = (List<Map<String, Object>>) contentResponse.get("parts");
                    return (String) partsResponse.get(0).get("text");
                }
            }
        } catch (Exception e) {
            System.err.println("Google Text Error: " + e.getMessage());
        }
        return "A luxurious custom jewelry piece designed just for you.";
    }

    // 2. IMAGE GENERATION (Imagen 4 - No Aspect Ratio Limit)
    public String generateImageUrl(String prompt) {
        try {
            System.out.println("Starting Google Imagen 4 Generation...");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + replicateApiKey);

            Map<String, Object> input = new HashMap<>();
            input.put("prompt", "cinematic product shot of " + prompt + ", professional jewelry photography, 8k, sharp focus, studio lighting, luxury");

            // REMOVED: input.put("aspect_ratio", "1:1");
            // The AI will now decide the best shape.

            input.put("safety_filter_level", "block_only_high");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("version", "google/imagen-4");
            requestBody.put("input", input);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(replicateUrl, entity, Map.class);

            if (response.getBody() == null) return getBackupImage();

            String getUrl = (String) ((Map<String, Object>) response.getBody().get("urls")).get("get");
            return pollForImage(getUrl, headers);

        } catch (Exception e) {
            System.err.println("Imagen Error: " + e.getMessage());
            e.printStackTrace();
            return getBackupImage();
        }
    }

    private String pollForImage(String url, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        int attempts = 0;
        while (attempts < 30) {
            try {
                Thread.sleep(1000);
                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                Map<String, Object> body = response.getBody();
                String status = (String) body.get("status");

                if ("succeeded".equals(status)) {
                    Object output = body.get("output");
                    if (output instanceof List) return ((List<String>) output).get(0);
                    if (output instanceof String) return (String) output;
                } else if ("failed".equals(status)) {
                    return getBackupImage();
                }
                attempts++;
            } catch (Exception e) { break; }
        }
        return getBackupImage();
    }

    private String getBackupImage() {
        return "https://loremflickr.com/500/500/jewelry?lock=" + System.currentTimeMillis();
    }
}