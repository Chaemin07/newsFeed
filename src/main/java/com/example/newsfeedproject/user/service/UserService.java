/*사용자 관련 비즈니스 로직을 처리하는 서비스 클래스. 회원가입, 프로필 수정, 탈퇴 처리 등을 담당
 */

package com.example.newsfeedproject.user.service;

import com.example.newsfeedproject.auth.dto.LoginRequestDto;
import com.example.newsfeedproject.auth.dto.LoginResponseDto;
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

	@Transactional
	public void signup(UserSignupRequestDto request) {
		if (!ValidationUtils.isValidEmail(request.getEmail())) {
			throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
		}
		if (!ValidationUtils.isValidPassword(request.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
		}
		if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
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

	public UserProfileResponseDto getProfile(Long userId) {
		User user = getActiveUserById(userId);

		return new UserProfileResponseDto(
				user.getId(),
				user.getEmail(),
				user.getNickname(),
				user.getBio(),
				user.getProfileImageUrl()
		);
	}

	@Transactional
	public void updateProfile(Long userId, UpdateProfileRequestDto request) {
		User user = getActiveUserById(userId);
		user.updateProfile(request.getNickname(), request.getBio(), request.getProfileImageUrl());
	}

	@Transactional
	public void deleteUser(Long userId, UserDeleteRequestDto request) {
		User user = getActiveUserById(userId);

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		user.markAsDeleted();
	}

	@Transactional
	public void updatePassword(Long userId, PasswordUpdateRequestDto request) {
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


	public LoginResponseDto authenticate(LoginRequestDto requestDto) {
		// 해당 email의 user없으면 throw
		User user = userRepository.findByEmail(requestDto.getUserEmail())
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		// user 가져와서 비밀번호 비교
		if (requestDto.getUserPassword() == null) {
			throw new RuntimeException("비밀번호가 입력되지 않았습니다.");
		}
		if (!passwordEncoder.matches(requestDto.getUserPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		// 로그인 성공
		LoginResponseDto responseDto = LoginResponseDto.builder()
				.userId(user.getId())
				.userName(user.getNickname()) // TODO 이름을 nickname으로 할건지, name으로 할건지
				.userEmail(user.getEmail())
				.isActive(!user.isDeleted())// 계정 활성 여부
				.build();
		return responseDto;
	}

	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("회원이 없습니다!"));
	}
}
