package com.vti.testing.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionResponse {

	private String status;
	private String message;
	public ExceptionResponse(String status, String message) {
		this.status = status;
		this.message = message;
	}
	
	

}
