package com.example.newsfeedproject.common.Filter;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 웹 애플리케이션의 서블릿 필터 설정을 담당하는 구성 클래스입니다.
 * <p>
 * 등록되는 필터:
 * <ul>
 *     <li>{@link RequestLoggingFilter} - 모든 요청에 대해 로그를 출력</li>
 *     <li>{@link LoginFilter} - 로그인 여부 확인</li>
 *     <li>{@link ActiveUserOnlyFilter} - 로그인된 사용자의 활성 상태 확인</li>
 * </ul>
 * 필터는 오름차순대로 실행됩니다.
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

    /**
     * 로그인된 사용자의 활성 상태를 검사하는 필터를 등록합니다.
     *
     * <p>모든 요청("/*")에 대해 {@link ActiveUserOnlyFilter}를 적용하며,
     * 필터 체인에서의 순서를 3으로 설정합니다.</p>
     *
     * @return 비활성 사용자 차단 필터가 등록된 {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean<ActiveUserOnlyFilter> inactiveUserFilter() {
        FilterRegistrationBean<ActiveUserOnlyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ActiveUserOnlyFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(3);
        return registrationBean;
    }

}
