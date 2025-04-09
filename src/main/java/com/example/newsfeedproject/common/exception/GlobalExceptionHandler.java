package com.example.newsfeedproject.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<String> handleCustomException(CustomException e) {
		return ResponseEntity
				.status(e.getErrorCode().getStatus())
				.body(e.getErrorCode().getMessage());
	}
}
