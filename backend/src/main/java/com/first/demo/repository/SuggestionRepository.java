package com.first.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.first.demo.dao.Suggestion;
import com.first.demo.dao.SuggestionStatus;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByUserUserId(Long userId);
    List<Suggestion> findByStatus(SuggestionStatus status); // SuggestionStatus 타입으로 변경
    List<Suggestion> findByCategory(String category);
}
