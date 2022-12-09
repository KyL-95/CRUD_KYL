package com.vti.testing.exception;

import com.vti.testing.exception.custom_exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExHandler {


    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
    public ExceptionResponse alreadyExistException(AlreadyExistEx e) {
        return new ExceptionResponse("Already exist !", e.getMessage());

    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)

    public ExceptionResponse categoryIdInvalidEx(CategoryIdInvalidEx e) {
        return new ExceptionResponse("Invalid id ! ", e.getMessage());

    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse categoryNameInvalidEx(CategoryNameInvalidEx e) {
        return new ExceptionResponse("Invalid name ! ", e.getMessage());

    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundEx(NotFoundEx e) {
        return new ExceptionResponse("Not Found! ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse exception(Exception e) {
        return new ExceptionResponse("An error occurred", e.getMessage());

    }


    // --------- JWT exception -----------
//    @org.springframework.web.bind.annotation.ExceptionHandler
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public ExceptionResponse invalidJWTEX(ExpiredEX e) {
//        return new ExceptionResponse("JWT is invalid", e.getMessage());
//    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ExceptionResponse expiredRefreshToken(TokenRefreshException e) {
        return new ExceptionResponse("Refresh Token Bad Request!", e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionResponse userNameNotFound(UsernameNotFoundException e) {
        return new ExceptionResponse("Login error", e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionResponse passWordIncorrect(PassWordUncorrectedEx e) {
        return new ExceptionResponse("Login error", e.getMessage());
    }
}
