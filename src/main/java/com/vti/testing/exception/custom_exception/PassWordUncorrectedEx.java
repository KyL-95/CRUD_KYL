package com.vti.testing.exception.custom_exception;

public class PassWordUncorrectedEx extends RuntimeException {
    public PassWordUncorrectedEx(String message) {
        super(message);
    }
}
