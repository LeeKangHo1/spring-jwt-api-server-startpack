package com.metacoding.restserver._core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.metacoding.restserver._core.auth.LoginUser;
import com.metacoding.restserver.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtUtil {

    // 컴퍼넌트 스캔시에 @Value가 발동하고, 해당 값은 application.properties에서 가져온다.
    @Value("${var.jwt.secret}")
    private String secret;

    // 토큰 만료 시간 설정
    public final Long EXPIRATION_TIME = 1000*60*60*24*2L;

    public String create(User user) {
        String jwt = JWT.create()
                .withSubject("title") // 토큰 이름
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withExpiresAt(Instant.now().plusMillis(EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret));
        return "Bearer " + jwt; // Bearer 이건 프로토콜
    }

    public LoginUser verify(String jwt)
            throws SignatureVerificationException, TokenExpiredException, JWTDecodeException {
        jwt = jwt.replace("Bearer ", "");

        // JWT를 검증한 후, 검증이 완료되면, header, payload를 base64로 복호화함.
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secret))
                .build().verify(jwt);

        int id = decodedJWT.getClaim("id").asInt();
        String username = decodedJWT.getClaim("username").asString();

        // 순수하게 인증용 토큰(payload)이니까 여기에 개인정보 등 민감한 정보를 주면 안 된다. 그런 정보는 DB와 통신을 거치도록 한다.
        return new LoginUser(id, username, jwt);
    }
}