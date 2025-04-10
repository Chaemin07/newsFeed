/* 사용자 관련 요청을 처리하는 REST 컨트롤러
 *회원가입, 프로필 조회/수정, 탈퇴 등을 정의
 */


package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.UserSignupRequestDto;
import com.example.newsfeedproject.user.dto.UpdateProfileRequestDto;
import com.example.newsfeedproject.user.dto.UserProfileResponseDto;
import com.example.newsfeedproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.newsfeedproject.user.dto.UserDeleteRequestDto;
import com.example.newsfeedproject.user.dto.PasswordUpdateRequestDto;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	/*
	 * 회원가입 요청을 처리
	 * @param signupRequest 사용자 정보 (이메일, 비밀번호, 이름 등)
	 * @return 성공 시 200 OK
	 */
	@PostMapping("/signup")
	public ResponseEntity<Void> join(@RequestBody UserSignupRequestDto request) {
		userService.signup(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long userId) {
		UserProfileResponseDto response = userService.getProfile(userId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<Void> updateProfile(
			@PathVariable Long userId,
			@RequestBody UpdateProfileRequestDto request) {
		userService.updateProfile(userId, request);
		return ResponseEntity.ok().build();
	}
	@PatchMapping("/{userId}/password")
	public ResponseEntity<Void> updatePassword(@PathVariable Long userId,
			@RequestBody PasswordUpdateRequestDto request) {
		userService.updatePassword(userId, request);
		return ResponseEntity.ok().build();
	}


	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
			@RequestBody UserDeleteRequestDto request) {
		userService.deleteUser(userId, request);
		return ResponseEntity.noContent().build();
	}
}
