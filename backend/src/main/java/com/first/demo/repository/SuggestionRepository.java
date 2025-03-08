package com.first.demo.repository;

import com.first.demo.domain.Suggestion;
import com.first.demo.domain.SuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByUserUserId(Long userId);
    List<Suggestion> findByStatus(SuggestionStatus status); // SuggestionStatus 타입으로 변경
    List<Suggestion> findByCategory(String category);
}
