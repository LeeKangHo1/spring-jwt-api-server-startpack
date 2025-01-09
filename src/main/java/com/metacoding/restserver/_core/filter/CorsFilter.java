package com.metacoding.restserver._core.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("debug: CorsFilter 작동됨~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 디버그 실행(플러터에서 크롬 디버그 등) 시 주소(특히 포트)가 계속 바뀌기 때문에 바뀌는 주소에 맞춰 적용하기 위해 작성
        String origin = request.getHeader("Origin"); // 배포 할 경우 Origin을 배포하는 url에 맞게 수정

        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Expose-Headers", "Authorization"); // 인증 헤더를 노출할 수 있도록 설정
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, OPTIONS"); // OPTIONS 필수
        response.setHeader("Access-Control-Max-Age", "3600"); // 프리플라이트 요청 결과를 캐시에 남겨두는 시간
        response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization"); // 클라이언트가 서버로 요청할 때 포함할 수 있는 헤더를 지정합니다.
        response.setHeader("Access-Control-Allow-Credentials", "true"); // true하면 쿠키랑 Authorization 전달 가능

        // Preflight 요청을 허용하고 바로 응답하는 코드
        // Preflight는 브라우저가 정해진 프로토콜에 따라 자동으로 보내는 것
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // options 요청이 오면 200ok 보냄
        }else {
            chain.doFilter(req, res);
        }
    }
}