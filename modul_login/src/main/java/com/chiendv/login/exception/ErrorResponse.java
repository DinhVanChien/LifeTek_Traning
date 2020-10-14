package com.chiendv.login.exception;

import org.springframework.http.HttpStatus;

/**
 * @author ChienDV
 *
 */
public class ErrorResponse {
	private HttpStatus status;
	private String message;
	
	public ErrorResponse() {
		super();
	}
	
	public ErrorResponse(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
