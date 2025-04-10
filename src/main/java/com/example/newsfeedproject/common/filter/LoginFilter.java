package com.example.newsfeedproject.common.filter;

import com.example.newsfeedproject.auth.dto.LoginResponseDto;
import com.example.newsfeedproject.auth.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;


/**
 * 로그인 필터 클래스.
 * <p>
 * 인증이 필요한 요청에 대해 로그인 여부를 확인하고,
 * 로그인되지 않은 사용자의 접근을 차단하는 역할을 합니다.
 * 로그인 없이 접근 가능한 URI(화이트리스트)는 검사 대상에서 제외됩니다.
 * </p>
 *
 * <p><b>주요 기능:</b></p>
 * <ul>
 *     <li>요청 URI가 인증이 필요한 경로인지 확인</li>
 *     <li>필요한 경우 세션을 조회하여 로그인 사용자 정보 확인</li>
 *     <li>로그인하지 않은 사용자라면 예외 발생</li>
 *     <li>정상 로그인 사용자라면 다음 필터로 요청 전달</li>
 * </ul>
 */
@Slf4j
public class LoginFilter implements Filter {
    /**
     * 로그인 없이 접근 가능한 URI 목록 (화이트리스트)
     * 이 경로들은 인증 검사에서 제외됩니다.
     */
    private static final String[] WHITE_LIST = {"/users", "/auth/login"};

    /**
     * 로그인 여부를 확인하는 필터 로직.
     * 인증이 필요한 URI에 대해 로그인된 세션이 없을 경우 예외를 발생시킵니다.
     *
     * @param request  서블릿 요청 객체
     * @param response 서블릿 응답 객체
     * @param chain    필터 체인
     * @throws IOException      입출력 예외 발생 시
     * @throws ServletException 서블릿 처리 예외 발생 시
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        log.info("로그인 필터 로직 실행 - 요청 URI: {}", requestURI);

        if (!isWhiteList(requestURI)) {
            LoginResponseDto loggedInUser = SessionManager.getLoggedInUser((HttpServletRequest) request);
            log.info("로그인된 사용자 ID {}, 요청URI: {}", loggedInUser.getUserId(), requestURI);
        }
        // 로그인된 사용자 또는 화이트리스트 경로일 경우 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }


    /**
     * 요청 URI가 로그인 없이 접근 가능한 화이트리스트에 포함되어 있는지 확인합니다.
     *
     * @param requestURI 현재 요청 URI
     * @return 화이트리스트에 포함되어 있으면 true, 그렇지 않으면 false
     */
    private boolean isWhiteList(String requestURI) {

        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

}



