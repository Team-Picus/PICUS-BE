package com.picus.core.infrastructure.stomp.resolver;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._UNAUTHORIZED;

@RequiredArgsConstructor
@Slf4j
public class StompCurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null
                && String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 헤더에서 토큰 꺼내기
        String token = tokenProvider.getToken(accessor)
                .orElseThrow(() -> {
                    log.error("토큰이 존재하지 않습니다. 세션 ID: {}", accessor.getSessionId());
                    return new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
                });

        return tokenProvider.getId(token)
                .orElseThrow(() -> new RestApiException(_UNAUTHORIZED));
    }
}
