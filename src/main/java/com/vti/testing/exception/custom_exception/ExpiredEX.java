package com.vti.testing.exception.custom_exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST, reason = "Token isssss Expired")
public class ExpiredEX extends RuntimeException {
    public ExpiredEX(String message) {
        super(message);
    }

    public ExpiredEX(String message, Throwable cause) {
        super(message, cause);
    }
}
