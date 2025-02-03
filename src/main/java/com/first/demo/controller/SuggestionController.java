package com.first.demo.controller;

import com.first.demo.domain.Suggestion;
import com.first.demo.service.SuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suggestions")
public class SuggestionController {

    private final SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Suggestion> createSuggestion(
            @PathVariable Long userId,
            @RequestParam String category,
            @RequestParam String suggestion) {
        Suggestion createdSuggestion = suggestionService.createSuggestion(userId, category, suggestion);
        return ResponseEntity.ok(createdSuggestion);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByUser(@PathVariable Long userId) {
        List<Suggestion> suggestions = suggestionService.getSuggestionsByUser(userId);
        return ResponseEntity.ok(suggestions);
    }

    // 상태 변경 요청 시 상태 검증이 포함됨
    @PutMapping("/{suggestionId}/status")
    public ResponseEntity<?> updateSuggestionStatus(
            @PathVariable Long suggestionId,
            @RequestParam String status,
            @RequestParam(required = false) String adminComments) {
        try {
            Suggestion updatedSuggestion = suggestionService.updateSuggestionStatus(suggestionId, status, adminComments);
            return ResponseEntity.ok(updatedSuggestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByStatus(@PathVariable String status) {
        List<Suggestion> suggestions = suggestionService.getSuggestionsByStatus(status);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByCategory(@PathVariable String category) {
        List<Suggestion> suggestions = suggestionService.getSuggestionsByCategory(category);
        return ResponseEntity.ok(suggestions);
    }
}

