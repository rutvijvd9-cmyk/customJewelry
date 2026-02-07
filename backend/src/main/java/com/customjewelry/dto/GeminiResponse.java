package com.customjewelry.dto;

import lombok.Data;
import java.util.List;

@Data
public class GeminiResponse {
    private List<Candidate> candidates;

    @Data
    public static class Candidate {
        private Content content;
    }

    @Data
    public static class Content {
        private List<Part> parts;
    }

    @Data
    public static class Part {
        private String text;
    }

    // Helper method to extract the text quickly
    public String getGeneratedText() {
        if (candidates != null && !candidates.isEmpty()) {
            return candidates.get(0).getContent().getParts().get(0).getText();
        }
        return "No content generated";
    }
}