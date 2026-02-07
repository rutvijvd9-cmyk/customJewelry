package com.customjewelry.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Document(collection = "jewelry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jewelry {

    @Id
    private String id;

    private String name;           // e.g., "Sunrise Ruby Ring"
    private String type;           // e.g., Ring, Necklace, Earring
    private String material;       // e.g., Gold, Silver, Platinum
    private String gemstone;       // e.g., Ruby, Diamond, Sapphire
    private Double weightGrams;    // e.g., 5.5
    private Double price;          // Estimated price

    // AI Generation Fields
    private String aiImagePrompt;  // The prompt sent to Gemini
    private String imageUrl;       // URL of the generated image

    private String description;
}