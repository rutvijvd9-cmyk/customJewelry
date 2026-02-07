package com.customjewelry.service;

import com.customjewelry.model.Jewelry;
import com.customjewelry.repository.JewelryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JewelryService {

    @Autowired
    private JewelryRepository repository;

    @Autowired
    private GeminiService geminiService;

    public List<Jewelry> getAllJewelry() {
        return repository.findAll();
    }

    public Optional<Jewelry> getJewelryById(String id) {
        return repository.findById(id);
    }

    // 3. Create a new design (Updated: Always generates image, even if fields are empty)
    public Jewelry createJewelry(Jewelry jewelry) {

        // Step 1: Handle Missing Prompt
        // If the user didn't write a prompt, we build one from the other fields.
        if (jewelry.getAiImagePrompt() == null || jewelry.getAiImagePrompt().trim().isEmpty()) {
            StringBuilder autoPrompt = new StringBuilder();

            // Add whatever info we have
            if (jewelry.getName() != null && !jewelry.getName().isEmpty())
                autoPrompt.append(jewelry.getName()).append(" ");

            if (jewelry.getMaterial() != null && !jewelry.getMaterial().isEmpty())
                autoPrompt.append("made of ").append(jewelry.getMaterial()).append(" ");

            if (jewelry.getType() != null && !jewelry.getType().isEmpty())
                autoPrompt.append(jewelry.getType()).append(" ");

            String finalPrompt = autoPrompt.toString().trim();

            // If the user literally left EVERYTHING empty, give a surprise
            if (finalPrompt.isEmpty()) {
                finalPrompt = "A unique, surprise luxury jewelry masterpiece";
            }

            jewelry.setAiImagePrompt(finalPrompt);
        }

        // Step 2: Generate Image (This will now ALWAYS run)
        // We use the prompt we just ensured exists
        String imageUrl = geminiService.generateImageUrl(jewelry.getAiImagePrompt());
        jewelry.setImageUrl(imageUrl);

        // Step 3: Generate Description (If missing)
        if (jewelry.getDescription() == null || jewelry.getDescription().isEmpty()) {
            String desc = geminiService.generateCreativeDescription(jewelry.getAiImagePrompt());
            jewelry.setDescription(desc);
        }

        return repository.save(jewelry);
    }

    // 4. Update an existing design
    public Jewelry updateJewelry(String id, Jewelry jewelryDetails) {
        return repository.findById(id).map(jewelry -> {
            jewelry.setName(jewelryDetails.getName());
            jewelry.setType(jewelryDetails.getType());
            jewelry.setMaterial(jewelryDetails.getMaterial());
            jewelry.setGemstone(jewelryDetails.getGemstone());
            jewelry.setWeightGrams(jewelryDetails.getWeightGrams());
            jewelry.setPrice(jewelryDetails.getPrice());
            jewelry.setAiImagePrompt(jewelryDetails.getAiImagePrompt());
            jewelry.setImageUrl(jewelryDetails.getImageUrl());
            jewelry.setDescription(jewelryDetails.getDescription());
            return repository.save(jewelry);
        }).orElseThrow(() -> new RuntimeException("Jewelry not found with id " + id));
    }

    public void deleteJewelry(String id) {
        repository.deleteById(id);
    }

    public List<Jewelry> searchByPrompt(String promptText) {
        return repository.findByAiImagePromptContaining(promptText);
    }

    public Jewelry updateImage(String id, String customPrompt) {
        Optional<Jewelry> existing = repository.findById(id);
        if (existing.isPresent()) {
            Jewelry item = existing.get();
            if (customPrompt != null && !customPrompt.isEmpty()) {
                item.setAiImagePrompt(customPrompt);
            }
            String newImageUrl = geminiService.generateImageUrl(item.getAiImagePrompt());
            item.setImageUrl(newImageUrl);
            return repository.save(item);
        }
        return null;
    }
}