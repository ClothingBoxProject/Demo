package com.first.demo.dao;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// User 엔티티 자체를 그대로 넘기지 않고,
// Spring Security가 요구하는 UserDetails 형식으로 감싸서 넘기기 
public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() { 
        return this.user; 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole())); 
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // TODO : 계정 만료 여부 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // TODO : 계정 잠금 여부 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // TODO : 계정 활성화 여부
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive();
    }
}