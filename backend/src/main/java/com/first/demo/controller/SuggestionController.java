package com.first.demo.controller;

import com.first.demo.dao.Suggestion;
import com.first.demo.dao.SuggestionStatus;
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

    @PutMapping("/{suggestionId}/status")
    public ResponseEntity<?> updateSuggestionStatus(
            @PathVariable Long suggestionId,
            @RequestParam SuggestionStatus status,  // SuggestionStatus로 받도록 변경
            @RequestParam(required = false) String adminComments) {
        try {
            // 서비스에서 상태 업데이트
            Suggestion updatedSuggestion = suggestionService.updateSuggestionStatus(suggestionId, status, adminComments);
            return ResponseEntity.ok(updatedSuggestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 상태를 SuggestionStatus ENUM으로 받도록 변경
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByStatus(@PathVariable SuggestionStatus status) {  // ENUM으로 변경
        List<Suggestion> suggestions = suggestionService.getSuggestionsByStatus(status);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByCategory(@PathVariable String category) {
        List<Suggestion> suggestions = suggestionService.getSuggestionsByCategory(category);
        return ResponseEntity.ok(suggestions);
    }
}
