/* 사용자 관련 요청을 처리하는 REST 컨트롤러
 * 회원가입, 프로필 조회/수정, 탈퇴 등을 정의
 */

package com.example.newsfeedproject.user.controller;


import com.example.newsfeedproject.auth.SessionManager;
import com.example.newsfeedproject.auth.dto.LoginResponseDto;
import com.example.newsfeedproject.user.dto.PasswordUpdateRequestDto;
import com.example.newsfeedproject.user.dto.UpdateProfileRequestDto;
import com.example.newsfeedproject.user.dto.UserDeleteRequestDto;
import com.example.newsfeedproject.user.dto.UserProfileResponseDto;
import com.example.newsfeedproject.user.dto.UserSignupRequestDto;
import com.example.newsfeedproject.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<Void> join(@RequestBody UserSignupRequestDto signupRequest) {
		userService.signup(signupRequest);
		return ResponseEntity.ok().build();
	}

	/*
	 * 사용자 프로필 조회 (다른 사람의 프로필도 가능)
	 * @param targetUserId 조회할 사용자 ID
	 * @return 조회된 사용자 프로필 정보
	 */
	@GetMapping("/{targetUserId}")
	public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long targetUserId) {
		UserProfileResponseDto response = userService.getProfile(targetUserId);
		return ResponseEntity.ok(response);
	}

	/*
	 * 로그인한 사용자의 프로필 수정
	 * @param request 현재 HTTP 요청 (세션에서 userId 조회)
	 * @param updateProfileRequest 변경할 닉네임, 소개, 이미지 정보
	 * @return 성공 시 200 OK
	 */
	@PutMapping("/profile")
	public ResponseEntity<Void> updateProfile(HttpServletRequest request,
			@RequestBody UpdateProfileRequestDto updateProfileRequest) {
		LoginResponseDto user = SessionManager.getLoginUser(request.getSession());
		Long userId = user.getUserId();
		userService.updateProfile(userId, updateProfileRequest);
		return ResponseEntity.ok().build();
	}

	/*
	 * 로그인한 사용자의 비밀번호 변경
	 * @param request 현재 HTTP 요청 (세션에서 userId 조회)
	 * @param passwordUpdateRequest 현재 비밀번호, 새 비밀번호
	 * @return 성공 시 200 OK
	 */
	@PatchMapping("/password")
	public ResponseEntity<Void> updatePassword(HttpServletRequest request,
			@RequestBody PasswordUpdateRequestDto passwordUpdateRequest) {
		LoginResponseDto user = SessionManager.getLoginUser(request.getSession());
		Long userId = user.getUserId();
		userService.updatePassword(userId, passwordUpdateRequest);
		return ResponseEntity.ok().build();
	}

	/*
	 * 로그인한 사용자 탈퇴 처리
	 * @param request 현재 HTTP 요청 (세션에서 userId 조회)
	 * @param userDeleteRequest 비밀번호 확인
	 * @return 성공 시 204 No Content
	 */
	@DeleteMapping
	public ResponseEntity<Void> deleteUser(HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody UserDeleteRequestDto userDeleteRequest) {
		HttpSession session = request.getSession(false);
		// 유효 세션 검증
		if (session == null) {
			throw new RuntimeException("로그인 세션이 없습니다.");
		}
		LoginResponseDto user = SessionManager.getLoginUser(session);
		Long userId = user.getUserId();
		userService.deleteUser(userId, userDeleteRequest);
		// 사용자 soft delete 이후 세션 만료 필요 -> 재로그인 요청
		SessionManager.logout(session);

		// 브라우저 세션 쿠키 삭제
		Cookie cookie = new Cookie("JSESSIONID", null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return ResponseEntity.noContent().build();
	}
}
