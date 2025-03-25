package com.picus.core.global.oauth.interceptor;

import com.picus.core.domain.client.application.TokenBlacklistUseCase;
import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.picus.core.global.common.exception.code.status.AuthErrorStatus.EMPTY_JWT;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JwtBlacklistInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final TokenBlacklistUseCase tokenBlacklistUseCase;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = tokenProvider.getToken(request)
                .orElseThrow(() -> new RestApiException(EMPTY_JWT));

        if (tokenBlacklistUseCase.isTokenInvalidated(token)) {
            response.setStatus(SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
