package com.first.demo.repository;

import com.first.demo.domain.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByUserUserId(Long userId);
    List<Suggestion> findByStatus(String status); // 특정 상태 조회
    List<Suggestion> findByCategory(String category);
}
