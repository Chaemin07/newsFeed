package com.example.newsfeedproject.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	//자기소개
	private String bio;
	private String profileImageUrl;

	//탈퇴 여부
	@Builder.Default
	private boolean deleted = false;

	//사용자 프로필 수정 닉네임, 자기소개, 이미지
	public void updateProfile(String nickname, String bio, String profileImageUrl) {
		this.nickname = nickname;
		this.bio = bio;
		this.profileImageUrl = profileImageUrl;
	}

	// 사용자 탈퇴처리
	public void markAsDeleted() {
		this.deleted = true;
	}
}
