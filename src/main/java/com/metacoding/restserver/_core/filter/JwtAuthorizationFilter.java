package com.metacoding.restserver._core.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.restserver._core.auth.LoginUser;
import com.metacoding.restserver._core.util.JwtUtil;
import com.metacoding.restserver._core.util.Resp;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("debug: JwtAuthorizationFilter 작동됨~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String jwt = request.getHeader("Authorization");

        if (jwt == null) {
            onError(response, "토큰 없음");
            return;
        }

        if (!jwt.startsWith("Bearer ")) {
            onError(response, "프로토콜 잘못됨(`Bearer `없음) 혹은 공백일 수 없다.");
            return;
        }

        try {
            LoginUser loginUser = jwtUtil.verify(jwt);

            HttpSession session = request.getSession();
            session.setAttribute("sessionUser", loginUser); // SessionUserResolver에서 설정함
        }catch (JWTDecodeException jwtDecodeException){
            onError(response, "토큰 검증 실패");
            return;
        }

        chain.doFilter(request, response);
    }

    private void onError(HttpServletResponse response, String msg) {
        try {
            // 여기서 setContentType까지는 스프링의 도움을 받지 못하기 때문에 직접 JSON으로 변환해야 한다.
            String responseBody = new ObjectMapper().writeValueAsString(Resp.fail(msg));
            // 설정 안하면 200
            response.setStatus(401);
            response.setContentType("application/json; charset=utf-8");

            PrintWriter out = response.getWriter();
            // 테스트도 실제 reponse 형식으로 나오게 해야 한다.
            out.println(responseBody);
        }catch (Exception e){
            // throw하면 안된다. 위임(다른 곳으로 보내서)할 곳 없다.
            e.printStackTrace();
        }

    }
}