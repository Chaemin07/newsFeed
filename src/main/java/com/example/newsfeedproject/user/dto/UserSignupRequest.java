/* 회원가입 시 클라이언트로부터 전달받는 사용자 정보(이메일, 비밀번호 등)를 담는 DTO.
 */

package com.example.newsfeedproject.user.dto;

import lombok.Getter;

@Getter
public class UserSignupRequest {

	private String email;
	private String password;
	private String nickname;
	private String bio;
	private String profileImageUrl;
}
