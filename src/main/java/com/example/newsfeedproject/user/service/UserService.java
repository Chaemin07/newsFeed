/*사용자 관련 비즈니스 로직을 처리하는 서비스 클래스. 회원가입, 프로필 수정, 탈퇴 처리 등을 담당
 */

package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.auth.Dto.LoginRequestDto;
import com.example.newsfeedproject.auth.Dto.LoginResponseDto;
import com.example.newsfeedproject.user.dto.*;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import com.example.newsfeedproject.common.exception.CustomException;
import com.example.newsfeedproject.common.exception.ErrorCode;
import com.example.newsfeedproject.common.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void signup(UserSignupRequest request) {
		if (!ValidationUtils.isValidEmail(request.getEmail())) {
			throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
		}
		if (!ValidationUtils.isValidPassword(request.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
		}
		if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}

		User user = User.builder()
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.nickname(request.getNickname())
				.bio(request.getBio())
				.profileImageUrl(request.getProfileImageUrl())
				.build();

		userRepository.save(user);
	}

	public UserProfileResponse getProfile(Long userId) {
		User user = getActiveUserById(userId);

		return new UserProfileResponse(
				user.getId(),
				user.getEmail(),
				user.getNickname(),
				user.getBio(),
				user.getProfileImageUrl()
		);
	}

	@Transactional
	public void updateProfile(Long userId, UpdateProfileRequest request) {
		User user = getActiveUserById(userId);
		user.updateProfile(request.getNickname(), request.getBio(), request.getProfileImageUrl());
	}

	@Transactional
	public void deleteUser(Long userId, UserDeleteRequest request) {
		User user = getActiveUserById(userId);

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		user.markAsDeleted();
	}

	@Transactional
	public void updatePassword(Long userId, PasswordUpdateRequest request) {
		User user = getActiveUserById(userId);

		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
		}

		if (!ValidationUtils.isValidPassword(request.getNewPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
		}

		user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
	}

	private User getActiveUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (user.isDeleted()) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		return user;
	}

	// TODO 구현해야함
	public LoginResponseDto authenticate(LoginRequestDto requestDto) {
		return null;
	}
}
