package com.example.newsfeedproject.common.utils;

import com.example.newsfeedproject.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 에러 응답 유틸 클래스.
 * <p>
 * 필터나 인터셉터 등에서 예외 발생 시,
 * 클라이언트에게 일관된 JSON 형식의 에러 메시지를 응답하기 위해 사용됩니다.
 * </p>
 */
public class ErrorResponseUtil {
    /**
     * 에러 정보를 JSON 형식으로 응답 본문에 작성하는 메서드.
     *
     * @param response  HttpServletResponse 객체
     * @param errorCode 에러 코드 (상태 코드 및 메시지를 포함한 enum)
     * @throws IOException 응답 본문 작성 중 예외 발생 시
     */
    public static void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        // HTTP 응답 상태 코드 설정
        response.setStatus(errorCode.getStatus().value());
        // 응답 콘텐츠 타입을 JSON으로 지정하고, UTF-8 인코딩을 명시
        response.setContentType("application/json; charset=UTF-8");
        // JSON 형태의 에러 메시지를 응답 본문에 작성
        response.getWriter().write("{\"status\":" + errorCode.getStatus().value()
                + ",\"message\":\"" + errorCode.getMessage() + "\"}");
    }
}
