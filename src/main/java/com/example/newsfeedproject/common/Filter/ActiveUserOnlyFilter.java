package com.example.newsfeedproject.common.Filter;

import com.example.newsfeedproject.auth.Dto.LoginResponseDto;
import com.example.newsfeedproject.auth.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 비활성 사용자(휴면 계정 또는 탈퇴 처리된 계정)의 요청을 차단하는 필터입니다.
 *
 * <p>
 * 로그인된 사용자 정보를 세션에서 조회하고,
 * {@link LoginResponseDto#isActive()} 값이 {@code false}일 경우
 * 요청을 차단하고 403 Forbidden 에러를 반환합니다.
 * </p>
 *
 * <p>
 * 이 필터는 로그인된 사용자의 유효성(활성 상태)을 검증하는 용도로 사용되며,
 * {@link SessionManager}를 통해 로그인 정보를 조회합니다.
 * </p>
 *
 * @see SessionManager#getLoggedInUser(HttpServletRequest)
 */
@Slf4j
public class ActiveUserOnlyFilter implements Filter {

    /**
     * 요청을 필터링하여 비활성 사용자일 경우 403 Forbidden 응답을 반환합니다.
     *
     * @param request  클라이언트 요청
     * @param response 서버 응답
     * @param chain    필터 체인
     * @throws IOException      입출력 예외 발생 시
     * @throws ServletException 서블릿 예외 발생 시
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        LoginResponseDto loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        try {
            LoginResponseDto user = SessionManager.getLoggedInUser(httpRequest);
            // 로그인되어 세션은 있는 상태
            // 로그인 된 유저의 상태 true: 유효한 사용자, false: 탈회한 사용자
            if (!user.isActive()) {
                log.info("비활성 사용자 차단: userId={}", user.getUserId());
                // 상태코드: 403, 로그인된 상태 but 권한 x
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "비활성 계정입니다.");
                return;
            }
        } catch (RuntimeException e) {
            // 로그인 안 된 사용자거나 세션 문제 → SessionManager에서 에러 로그와 예외 throw
        }

        chain.doFilter(request, response);
    }
}
