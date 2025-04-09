/* 사용자 관련 요청을 처리하는 REST 컨트롤러
 * 회원가입, 프로필 조회/수정, 탈퇴 등을 정의
 */

package com.example.newsfeedproject.user.controller;

import com.example.newsfeedproject.user.dto.UserSignupRequest;
import com.example.newsfeedproject.user.dto.UpdateProfileRequest;
import com.example.newsfeedproject.user.dto.UserProfileResponse;
import com.example.newsfeedproject.user.dto.UserDeleteRequest;
import com.example.newsfeedproject.user.dto.PasswordUpdateRequest;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;


	@PostMapping("/signup")
	public ResponseEntity<Void> join(@RequestBody UserSignupRequest signupRequest) {
		userService.signup(signupRequest);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{targetUserId}")
	public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long targetUserId) {
		UserProfileResponse response = userService.getProfile(targetUserId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/profile")
	public ResponseEntity<Void> updateProfile(HttpServletRequest request, @RequestBody UpdateProfileRequest updateProfileRequest) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		userService.updateProfile(userId, updateProfileRequest);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/password")
	public ResponseEntity<Void> updatePassword(HttpServletRequest request, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		userService.updatePassword(userId, passwordUpdateRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteUser(HttpServletRequest request, @RequestBody UserDeleteRequest userDeleteRequest) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		userService.deleteUser(userId, userDeleteRequest);
		return ResponseEntity.noContent().build();
	}
}
