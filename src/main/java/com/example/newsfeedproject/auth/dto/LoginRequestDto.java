package com.example.newsfeedproject.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 로그인 요청 시 클라이언트로부터 전달받는 사용자 입력 정보를 담는 DTO 클래스입니다.
 * <p>
 * 사용자 ID(email)와 비밀번호를 포함하며, 이 정보를 통해 로그인 인증을 수행합니다.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public class LoginRequestDto {
    /**
     * 사용자의 로그인 ID(email)
     */
    public final String userEmail;
    /**
     * 사용자의 비밀번호
     */
    public final String userPassword;

}
