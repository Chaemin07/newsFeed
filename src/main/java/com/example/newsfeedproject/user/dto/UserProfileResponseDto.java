/* 사용자 프로필 조회 시 클라이언트에게 반환할 사용자 정보를 담는 응답 DTO
Request DTO는 스프링이 JSON을 자동으로 매핑해주기 때문에 생성자가 필요 없고
Response DTO는 개발자가 직접 new로 생성하므로 모든 필드를 받는 생성자가 필요해서 @AllArgsConstructor를 사용
 */

package com.example.newsfeedproject.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {

	private Long id;
	private String email;
	private String nickname;
	private String bio;
	private String profileImageUrl;
}
