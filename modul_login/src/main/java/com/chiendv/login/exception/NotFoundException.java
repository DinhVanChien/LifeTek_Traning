package com.chiendv.login.exception;

/**
 * @author ADMIN
 * Class này tương ứng exception NotFound
 */
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 5083210677670843676L;

	public NotFoundException(String message) {
		super(message);
	}
}
