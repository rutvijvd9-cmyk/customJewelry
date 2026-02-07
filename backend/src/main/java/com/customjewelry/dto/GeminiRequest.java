package com.customjewelry.dto;

import lombok.Data;
import java.util.Collections;
import java.util.List;

@Data
public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String prompt) {
        this.contents = Collections.singletonList(new Content(prompt));
    }

    @Data
    public static class Content {
        private List<Part> parts;

        public Content(String text) {
            this.parts = Collections.singletonList(new Part(text));
        }
    }

    @Data
    public static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }
    }
}