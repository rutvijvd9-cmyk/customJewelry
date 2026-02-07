package com.customjewelry.controller;

import com.customjewelry.model.Jewelry;
import com.customjewelry.service.JewelryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jewelry")
@CrossOrigin(origins = "*") // Allows your frontend to access this API
public class JewelryController {

    @Autowired
    private JewelryService service;

    // 1. Get All Designs
    @GetMapping
    public List<Jewelry> getAllJewelry() {
        return service.getAllJewelry();
    }

    // 2. Get Single Design by ID
    @GetMapping("/{id}")
    public Optional<Jewelry> getJewelryById(@PathVariable String id) {
        return service.getJewelryById(id);
    }

    // 3. Create New Design
    @PostMapping
    public Jewelry createJewelry(@RequestBody Jewelry jewelry) {
        return service.createJewelry(jewelry);
    }

    // 4. Update Design
    @PutMapping("/{id}")
    public Jewelry updateJewelry(@PathVariable String id, @RequestBody Jewelry jewelry) {
        return service.updateJewelry(id, jewelry);
    }

    // 5. Delete Design
    @DeleteMapping("/{id}")
    public void deleteJewelry(@PathVariable String id) {
        service.deleteJewelry(id);
    }

    // 6. Search by AI Prompt
    @GetMapping("/search")
    public List<Jewelry> searchByPrompt(@RequestParam String prompt) {
        return service.searchByPrompt(prompt);
    }

    // 7. Regenerate Image Only
    // PUT http://localhost:8080/api/jewelry/{id}/image
    // 7. Regenerate Image (With optional new prompt)
    @PutMapping("/{id}/image")
    public Jewelry regenerateImage(@PathVariable String id, @RequestBody(required = false) String customPrompt) {
        return service.updateImage(id, customPrompt);
    }
}