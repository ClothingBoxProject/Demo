package com.first.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.first.demo.dao.RefreshToken;
import com.first.demo.dao.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<List<RefreshToken>> findAllByUser(User user);
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.user = :user")
    void deleteByUser(@Param("user") User user); //특정 사용자(User)의 모든 RefreshToken을 삭제
}
