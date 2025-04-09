/* 사용자 프로필 조회 시 클라이언트에게 반환할 사용자 정보를 담는 응답 DTO
 */

package com.example.newsfeedproject.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

	private Long id;
	private String email;
	private String nickname;
	private String bio;
	private String profileImageUrl;
}
