package com.first.demo.service;

import com.first.demo.dao.Suggestion;
import com.first.demo.dao.SuggestionStatus;
import com.first.demo.dao.User;
import com.first.demo.repository.SuggestionRepository;
import com.first.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final UserRepository userRepository;

    public SuggestionService(SuggestionRepository suggestionRepository, UserRepository userRepository) {
        this.suggestionRepository = suggestionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Suggestion createSuggestion(Long userId, String category, String suggestionText) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
        }
        User user = userOptional.get();
        Suggestion suggestion = new Suggestion();
        suggestion.setUser(user);
        suggestion.setCategory(category);
        suggestion.setSuggestion(suggestionText);
        suggestion.setStatus(SuggestionStatus.PENDING); // 기본 상태는 PENDING
        return suggestionRepository.save(suggestion);
    }

    @Transactional(readOnly = true)
    public List<Suggestion> getSuggestionsByUser(Long userId) {
        return suggestionRepository.findByUserUserId(userId);
    }

    @Transactional
    public Suggestion updateSuggestionStatus(Long suggestionId, SuggestionStatus status, String adminComments) {
        Optional<Suggestion> suggestionOptional = suggestionRepository.findById(suggestionId);
        if (suggestionOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 제안이 존재하지 않습니다.");
        }

        Suggestion suggestion = suggestionOptional.get();

        // REJECTED 상태일 때 adminComments 필수
        if (status == SuggestionStatus.REJECTED && (adminComments == null || adminComments.trim().isEmpty())) {
            throw new IllegalArgumentException("제안을 거절할 때는 관리자 코멘트가 필요합니다.");
        }

        suggestion.setStatus(status);
        suggestion.setAdminComments(adminComments);
        return suggestionRepository.save(suggestion);
    }

    @Transactional(readOnly = true)
    public List<Suggestion> getSuggestionsByStatus(SuggestionStatus status) {
        return suggestionRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Suggestion> getSuggestionsByCategory(String category) {
        return suggestionRepository.findByCategory(category);
    }
}
