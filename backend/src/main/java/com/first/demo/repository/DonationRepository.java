package com.first.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.first.demo.dao.Donation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByUserUserId(Long userId);

    // 기부 일시(donatedAt)만 조회하는 메서드 추가
    @Query("SELECT d.donatedAt FROM Donation d WHERE d.user.userId = :userId")
    List<LocalDateTime> findDonationDatesByUserId(Long userId);
}
