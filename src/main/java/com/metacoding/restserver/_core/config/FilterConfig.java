package com.metacoding.restserver._core.config;

import com.metacoding.restserver._core.filter.CorsFilter;
import com.metacoding.restserver._core.filter.JwtAuthorizationFilter;
import com.metacoding.restserver._core.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthorizationFilter() {
        System.out.println("debug: jwtAuthorizationFilter 등록됨~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        // new JwtAuthorizationFilter(jwtUtil) 하는 이유 -> filter는 계속 new해야 되지만 JwtUtil은 1개만 있어도 된다.
        FilterRegistrationBean<JwtAuthorizationFilter> bean = new FilterRegistrationBean<>(new JwtAuthorizationFilter(jwtUtil));
        bean.addUrlPatterns("/api/*"); // 인증 필요한 곳은 다 /api를 추가시키는 게 편하다.
        bean.setOrder(1); // 0번인 CorsFilter랑 겹치지 않게
        return bean;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        System.out.println("debug: CorsFilter 등록됨~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter());
        bean.addUrlPatterns("/*"); // * 하나만 써야됨.
        bean.setOrder(0); // 낮은 번호부터 실행됨. -> CORS 부터 실행
        return bean;
    }
}