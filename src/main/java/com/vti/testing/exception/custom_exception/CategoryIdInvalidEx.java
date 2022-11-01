package com.vti.testing.exception.custom_exception;

public class CategoryIdInvalidEx extends RuntimeException{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CategoryIdInvalidEx(String message) {
		super(message);
	}
}
