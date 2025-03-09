package com.first.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Service;
import com.first.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
            .map(user -> User.builder() //UserDetails 생성하기 
                    .username(user.getUserName())
                    .password(user.getPasswordHash()) 
                    .roles("USER")  
                    .build())
            .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
