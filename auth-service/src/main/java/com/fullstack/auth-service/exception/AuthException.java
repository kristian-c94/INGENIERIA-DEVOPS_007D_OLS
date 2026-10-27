package com.fullstack.auth-service.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
