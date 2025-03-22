package com.first.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.first.demo.dao.CustomUserDetails;
import com.first.demo.dao.User; 
import com.first.demo.repository.UserRepository; 

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    //UserDetailsService는 Spring Security가 제공하는 인터페이스

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
        return new CustomUserDetails(user);
    }
}
