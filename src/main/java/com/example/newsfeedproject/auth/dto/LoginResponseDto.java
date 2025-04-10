package com.example.newsfeedproject.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 성공 시 클라이언트에 반환되는 사용자 정보 DTO입니다.
 * <p>
 * 사용자 식별 정보와 기본 프로필 정보를 포함합니다.
 * 이 정보는 로그인 상태 확인 API 응답으로 사용될 수 있습니다.
 * </p>
 */
@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    /**
     * 사용자의 고유 ID
     */
    private Long userId;
    /**
     * 사용자의 이름
     */
    private String userName;
    /**
     * 사용자의 이메일 주소
     */
    private String userEmail;

    private boolean isActive;



}
