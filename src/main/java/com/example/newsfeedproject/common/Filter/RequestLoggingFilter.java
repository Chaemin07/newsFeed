package com.example.newsfeedproject.common.Filter;

import com.example.newsfeedproject.auth.Dto.LoginResponseDto;
import com.example.newsfeedproject.auth.SessionManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 요청에 대한 로그를 남기는 필터입니다.
 * <p>
 * 로그인된 사용자가 존재하면 userId를 포함한 요청 로그를 출력하고,
 * 로그인되지 않은 경우에는 userId 없이 요청 로그를 출력합니다.
 * <p>
 * 이 필터는 {@link SessionManager}를 사용하여 세션에서 사용자 정보를 가져옵니다.
 * <p>
 * 로그 형식:
 * - 로그인된 사용자: userId={id} REQUEST , method=[METHOD], uri=[URI]
 * - 비로그인 사용자: ??? REQUEST, method=[METHOD], uri=[URI]
 */
@Slf4j
public class RequestLoggingFilter implements Filter {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 요청을 가로채어 로그를 출력한 후, 다음 필터로 체인을 넘깁니다.
     *
     * @param request  클라이언트 요청
     * @param response 서버 응답
     * @param chain    필터 체인
     * @throws IOException      입출력 예외 발생 시
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            LoginResponseDto user = SessionManager.getLoggedInUser(httpRequest);
            Long userId = user.getUserId();

            String method = httpRequest.getMethod();
            String requestURI = httpRequest.getRequestURI();
            // timestamp는 혹시 모르는 db 저장용, 사용은 안함
            String timestamp = LocalDateTime.now().format(formatter);

            log.info("userId={} REQUEST , method= [{}], uri= [ {} ]", userId, method, requestURI);

        } catch (RuntimeException e) {
            String method = httpRequest.getMethod();
            String requestURI = httpRequest.getRequestURI();

            // 로그인 정보가 없는 경우
            log.info("??? REQUEST, method=[{}], uri=[{}]", method, requestURI);

        }

        chain.doFilter(request, response);

    }
}
