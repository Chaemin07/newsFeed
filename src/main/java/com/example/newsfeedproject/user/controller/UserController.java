/* 사용자 관련 요청을 처리하는 REST 컨트롤러
 *회원가입, 프로필 조회/수정, 탈퇴 등을 정의
 */


package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.UserSignupRequest;
import com.example.newsfeedproject.user.dto.UpdateProfileRequest;
import com.example.newsfeedproject.user.dto.UserProfileResponse;
import com.example.newsfeedproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.newsfeedproject.user.dto.UserDeleteRequest;
import com.example.newsfeedproject.user.dto.PasswordUpdateRequest;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<Void> join(@RequestBody UserSignupRequest request) {
		userService.signup(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long userId) {
		UserProfileResponse response = userService.getProfile(userId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<Void> updateProfile(
			@PathVariable Long userId,
			@RequestBody UpdateProfileRequest request) {
		userService.updateProfile(userId, request);
		return ResponseEntity.ok().build();
	}
	@PatchMapping("/{userId}/password")
	public ResponseEntity<Void> updatePassword(@PathVariable Long userId,
			@RequestBody PasswordUpdateRequest request) {
		userService.updatePassword(userId, request);
		return ResponseEntity.ok().build();
	}


	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
			@RequestBody UserDeleteRequest request) {
		userService.deleteUser(userId, request);
		return ResponseEntity.noContent().build();
	}
}
