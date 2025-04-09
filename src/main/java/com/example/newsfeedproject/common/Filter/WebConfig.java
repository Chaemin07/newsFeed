package com.example.newsfeedproject.common.Filter;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 웹 애플리케이션의 서블릿 필터 설정을 담당하는 구성 클래스입니다.
 * <p>
 * 요청 로깅 필터 및 로그인 필터를 전체 요청 경로에 등록하고,
 * 필터 체인 내 실행 순서를 정의합니다.
 * </p>
 */
@Configuration
public class WebConfig {

    /**
     * 요청 정보를 로그로 출력하는 필터를 등록하는 설정 메서드입니다.
     *
     * <p>모든 요청("/*")에 대해 {@link RequestLoggingFilter}를 적용하며,
     * 필터 체인에서의 순서를 1로 설정합니다.</p>
     *
     * @return 요청 로깅 필터가 등록된 {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLoggingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * 로그인 필터를 등록하는 설정 메서드입니다.
     *
     * <p>모든 요청("/*")에 대해 {@link LoginFilter}를 적용하며,
     * 필터 체인에서의 순서를 2로 설정합니다.</p>
     *
     * @return 로그인 필터가 등록된 {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilter() {
        FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

}
