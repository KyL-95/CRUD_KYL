	package com.vti.testing.exception;

import com.vti.testing.exception.custom_exception.AlreadyExistEx;
import com.vti.testing.exception.custom_exception.CategoryIdInvalidEx;
import com.vti.testing.exception.custom_exception.CategoryNameInvalidEx;
import com.vti.testing.exception.custom_exception.NotFoundEx;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {


	@org.springframework.web.bind.annotation.ExceptionHandler
	 @ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
	public ExceptionResponse alreadyExistException(AlreadyExistEx e) {
		return new ExceptionResponse("Already exist !", e.getMessage());

	}

	@org.springframework.web.bind.annotation.ExceptionHandler
	public ExceptionResponse categoryIdInvalidEx(CategoryIdInvalidEx e) {
		return new ExceptionResponse("Invalid id ! ", e.getMessage());

	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ExceptionResponse categoryNameInvalidEx(CategoryNameInvalidEx e) {
		return new ExceptionResponse("Invalid name ! ", e.getMessage());

	}

	@org.springframework.web.bind.annotation.ExceptionHandler
	public ExceptionResponse exception(Exception e) {
		return new ExceptionResponse("An error occurred", e.getMessage());
			
	}

}
