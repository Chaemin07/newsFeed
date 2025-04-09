/* 비밀번호 변경 요청 시 사용되는 DTO. 현재 비밀번호, 새 비밀번호 정보를 포함
 */

package com.example.newsfeedproject.user.dto;

import lombok.Getter;

@Getter
public class PasswordUpdateRequest {

	private String currentPassword;
	private String newPassword;
}
