package com.chiendv.login.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * @author ADMIN
 * 
 */
@RestControllerAdvice
public class CustomExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handlerNotFoundException(NotFoundException ex, WebRequest web) {
		return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}
	
	// Xử lý tất cả các exception chưa được khai báo
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception ex, WebRequest req) {
        // Log err
        ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
