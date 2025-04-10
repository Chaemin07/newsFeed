/*회원 탈퇴 요청 시 필요한 정보를 담는 DTO. 주로 비밀번호 확인 용도로 사용.
 */

package com.example.newsfeedproject.user.dto;

import lombok.Getter;

@Getter
public class UserDeleteRequestDto {

	private String password;
}
