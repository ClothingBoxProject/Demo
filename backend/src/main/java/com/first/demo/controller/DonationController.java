package com.first.demo.controller;

import com.first.demo.dao.Donation;
import com.first.demo.service.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Donation> createDonation(@PathVariable Long userId) {
        Donation donation = donationService.createDonation(userId);
        return ResponseEntity.ok(donation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Donation>> getDonationsByUser(@PathVariable Long userId) {
        List<Donation> donations = donationService.getDonationsByUser(userId);
        return ResponseEntity.ok(donations);
    }
    // 추가: 사용자의 기부 일시만 조회
    @GetMapping("/user/{userId}/donation-dates")
    public ResponseEntity<List<LocalDateTime>> getDonationDatesByUser(@PathVariable Long userId) {
        List<LocalDateTime> donationDates = donationService.getDonationDatesByUser(userId);
        return ResponseEntity.ok(donationDates);
    }
}
