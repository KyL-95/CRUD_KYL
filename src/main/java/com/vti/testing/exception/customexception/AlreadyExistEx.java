package com.vti.testing.exception.customexception;

public class AlreadyExistEx extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public AlreadyExistEx(String message) {
		super(message);
	}
}
