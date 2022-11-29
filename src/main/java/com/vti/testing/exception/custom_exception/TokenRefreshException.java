package com.vti.testing.exception.custom_exception;

public class TokenRefreshException extends RuntimeException{

    public TokenRefreshException(String message) {
        super(message);
    }
}
