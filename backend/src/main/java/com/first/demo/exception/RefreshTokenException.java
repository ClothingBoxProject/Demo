package com.first.demo.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class RefreshTokenException extends RuntimeException {
    private final HttpStatus status;
    public RefreshTokenException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
