package com.vti.testing.exception.custom_exception;

public class AlreadyExistEx extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public AlreadyExistEx(String message) {
		super(message);
	}
}
