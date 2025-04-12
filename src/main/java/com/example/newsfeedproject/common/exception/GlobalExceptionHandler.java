package com.example.newsfeedproject.common.exception;

import com.example.newsfeedproject.common.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 커스텀 예외 처리
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity
				.status(errorCode.getStatus())
				.body(ApiResponse.error(errorCode.getStatus().value(), errorCode.getMessage()));
	}

	//valid 위반 통합에러 처리기
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return ResponseEntity
				.badRequest()// 400
				.body(ApiResponse.error(HttpStatus.BAD_REQUEST, "요청 필드값이 유효하지 않습니다."));
	}

	//validated 위반 통합 에러 처리기
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
		return ResponseEntity
				.badRequest()
				.body(ApiResponse.error(HttpStatus.BAD_REQUEST, "입력값이 제약조건을 만족하지 않습니다."));
	}

	// 런타임 에러
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
		return ResponseEntity
				.internalServerError()// 500 서버 에러
				.body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다: " + e.getMessage()));
	}

	// 모든 예외처리 - 마지막 예외처리
	public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
		return ResponseEntity
				.internalServerError() // 500 서버 에러
				.body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버오류가 발생했습니다."));
	}
}
