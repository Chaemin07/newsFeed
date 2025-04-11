package com.example.newsfeedproject.common.utils;

import com.example.newsfeedproject.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorResponseUtil {
    public static void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write("{\"status\":" + errorCode.getStatus().value()
                + ",\"message\":\"" + errorCode.getMessage() + "\"}");
    }
}
