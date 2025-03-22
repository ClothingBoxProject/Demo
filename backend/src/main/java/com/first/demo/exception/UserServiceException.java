package com.first.demo.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UserServiceException extends RuntimeException {
    private final HttpStatus status;
    public UserServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
