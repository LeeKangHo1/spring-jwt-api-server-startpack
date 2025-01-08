package com.metacoding.restserver._core.auth;

import com.metacoding.restserver.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Configuration
public class SessionUserResolver implements HandlerMethodArgumentResolver {
    private final HttpSession session;

    // AOP로 하는 것을 편하게 하라고 만드는 것
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @SessionUser 어노테이션이 달려 있고 User class와 일치하면 resolveArgument 발동
        boolean isAnnotated = parameter.getParameterAnnotation(SessionUser.class) != null;
        boolean isClass = User.class.equals(parameter.getParameterType());
        return isAnnotated && isClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 이렇게 하면 컨트롤러 메서드에서 @SessionUser User user와 같이 선언된 파라미터에 세션 사용자 정보가 자동으로 주입됩니다.
        User sessionUser = (User) session.getAttribute("sessionUser");
        return sessionUser;
    }
}