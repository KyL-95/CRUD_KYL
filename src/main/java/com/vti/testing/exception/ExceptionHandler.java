package com.vti.testing.exception;

import java.util.NoSuchElementException;

import com.vti.testing.exception.customexception.AlreadyExistEx;
import com.vti.testing.exception.customexception.NotFoundEx;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
@ControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler
	public ExceptionResponse noSuchElement(NoSuchElementException e) {
		return new ExceptionResponse("", "This product is not exist") ;
		
	}
				
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ExceptionResponse exception(Exception e) {
		return new ExceptionResponse("An error occurred", e.getMessage()) ;
		
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ExceptionResponse notFoundException(NotFoundEx e) {
		return new ExceptionResponse("Not Found !", e.getMessage()) ;
		
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler
	public ExceptionResponse alreadyExistException(AlreadyExistEx e) {
		return new ExceptionResponse("Already exist !", e.getMessage()) ;
		
	}
	
	
	
}
