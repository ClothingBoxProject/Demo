package com.first.demo.service;

import com.first.demo.domain.Donation;
import com.first.demo.domain.User;
import com.first.demo.repository.DonationRepository;
import com.first.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    public DonationService(DonationRepository donationRepository, UserRepository userRepository) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Donation createDonation(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
        }
        User user = userOptional.get();
        Donation donation = new Donation();
        donation.setUser(user);
        return donationRepository.save(donation);
    }

    @Transactional(readOnly = true)
    public List<Donation> getDonationsByUser(Long userId) {
        return donationRepository.findByUserUserId(userId);
    }
}
