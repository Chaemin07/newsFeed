/* 사용자의 닉네임, 자기소개, 프로필 이미지 등을 수정할 때 사용하는 요청 DTO.
 */

package com.example.newsfeedproject.user.dto;

import lombok.Getter;

@Getter
public class UpdateProfileRequest {

	private String name;
	private String bio;
	private String profileImageUrl;
}
