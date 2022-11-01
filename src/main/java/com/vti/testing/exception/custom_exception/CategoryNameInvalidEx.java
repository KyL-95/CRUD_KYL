package com.vti.testing.exception.custom_exception;

public class CategoryNameInvalidEx extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public CategoryNameInvalidEx(String message) {
		super(message);
	}
}
