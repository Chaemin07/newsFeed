package com.example.newsfeedproject.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	// 어떤 컨트롤러에서 사용중인 에러코드다 라는 것을 명시해주기
	INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
	INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다."),
	// 로그인 필요 - 로그인 필터 에러코드
	LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
	// 탈퇴한 사용자 접근 - ActiveUserOnly필터 에러코드
	DEACTIVATED_USER(HttpStatus.FORBIDDEN, "비활성 사용자입니다. 로그인이 제한됩니다."),

	// FeedService에 게시글 삭제 부분에서 사용중!
	UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, "작성자만 수정/삭제할 수 있습니다.");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
