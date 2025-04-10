package com.example.newsfeedproject.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

	@ExceptionHandler(MethodArgumentNotValidException.class)//valid 위반 통합에러 처리기
	public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return ResponseEntity.badRequest().body("Global Error: Required values must not blank and have proper value.");
	}
	@ExceptionHandler(ConstraintViolationException.class)//validated 위반 통합 에러 처리기
	public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
		return ResponseEntity.badRequest().body("Global Error: browsing target must be specified.");
	}
}
