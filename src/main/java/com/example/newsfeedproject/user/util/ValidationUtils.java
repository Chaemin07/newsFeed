/*이메일 형식, 비밀번호 유효성 검사 등 사용자 입력값에 대한 검증 로직을 제공하는 유틸리티 클래스.
 */

package com.example.newsfeedproject.user.util;

import java.util.regex.Pattern;

public class ValidationUtils {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$");
	private static final Pattern PASSWORD_PATTERN =
			Pattern.compile(
					"^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=[\\]{};':\"\\\\|,.<>/?]).{8,}$");

	public static boolean isValidEmail(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

	public static boolean isValidPassword(String password) {
		return PASSWORD_PATTERN.matcher(password).matches();
	}
}
