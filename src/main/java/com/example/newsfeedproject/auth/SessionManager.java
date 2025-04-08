package com.example.newsfeedproject.auth;

import com.example.newsfeedproject.auth.Dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 세션을 이용하여 로그인 상태 관리하는 유틸리티 클래스입니다.
 * <p>
 * 세션을 통해 로그인된 사용자 정보를 저장하고,
 * 로그인 여부 확인, 로그아웃 처리 등 로그인 관련 기능을 정적으로 제공합니다.
 * 상태를 가지지 않는 유틸리티 클래스로 정적인 기능(유틸성 기능)만 제공
 * </p>
 */
@Slf4j
public class SessionManager {
    /**
     * 유틸리티 클래스이므로 인스턴스를 생성할 수 없습니다.
     */
    private SessionManager() {
        throw new UnsupportedOperationException("SessionManager는 유틸 클래스입니다.");
    }

    /**
     * 세션에 저장될 사용자 정보 키
     */
    public static final String LOGIN_USER = "loginUser";

    /**
     * 1분의 초를 의미하는 상수.
     */
    public static final int seconds = 60;
    /**
     * 세션 유지 시간을 초 단위로 계산한 값 (30분).
     */
    public static final int minutes = 30 * seconds;

    /**
     * 로그인한 사용자 정보를 세션에 저장하고, 세션 만료 시간을 설정합니다.
     *
     * @param session 현재 사용자의 HttpSession
     * @param user    로그인한 사용자 정보 DTO
     */
    public static void setLoginUser(HttpSession session, LoginResponseDto user) {
        session.setAttribute(LOGIN_USER, user);
        session.setMaxInactiveInterval(minutes);
    }

    /**
     * 현재 세션을 무효화하여 로그아웃 처리합니다.
     *
     * @param session 현재 사용자의 HttpSession
     */
    public static void logout(HttpSession session) {
        session.invalidate();
    }

    /**
     * 세션에 저장된 로그인 사용자 정보를 반환합니다.
     *
     * @param session 현재 사용자의 HttpSession
     * @return 세션에 저장된 사용자 정보, 없으면 null
     */
    public static LoginResponseDto getLoginUser(HttpSession session) {
        return (LoginResponseDto) session.getAttribute(LOGIN_USER);
    }

    /**
     * 사용자가 로그인 상태인지 확인합니다.
     *
     * @param session 현재 사용자의 HttpSession
     * @return 로그인 상태이면 true, 아니면 false
     */
    public static boolean isLogin(HttpSession session) {
        return session != null && getLoginUser(session) != null;
    }

    /**
     * HttpServletRequest로부터 로그인된 사용자 정보를 조회합니다.
     * <p>
     * 세션이 없거나 로그인 정보가 존재하지 않는 경우 예외를 발생시킵니다.
     * 인증이 필요한 요청에서 반드시 로그인 상태임을 보장받고자 할 때 사용합니다.
     * </p>
     *
     * @param request 현재 요청 객체
     * @return 로그인된 사용자 정보
     * @throws RuntimeException 로그인되지 않은 경우
     */
    public static LoginResponseDto getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        LoginResponseDto loggedUser = (session != null) ?
                (LoginResponseDto) session.getAttribute(LOGIN_USER) : null;

        if (loggedUser == null) {
            log.warn("비로그인 사용자 접근 차단 - 요청 URI: {}", requestURI);
            throw new RuntimeException("로그인 해주세요.");
        }
        return loggedUser;
    }

}
