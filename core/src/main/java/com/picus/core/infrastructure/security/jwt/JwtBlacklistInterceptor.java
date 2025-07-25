package com.picus.core.infrastructure.security.jwt;

import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.TokenValidationQueryPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.picus.core.shared.exception.code.status.AuthErrorStatus.*;

@Component
@RequiredArgsConstructor
public class JwtBlacklistInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final TokenValidationQueryPort tokenValidationQueryPort;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 유저 정보 조회
        String token = tokenProvider.getToken(request)
                .orElseThrow(() -> new RestApiException(EMPTY_JWT));

        // 블랙리스트 검사
        if (tokenValidationQueryPort.isBlacklistToken(token))
            throw new RestApiException(EXPIRED_MEMBER_JWT);

        return true;
    }
}
