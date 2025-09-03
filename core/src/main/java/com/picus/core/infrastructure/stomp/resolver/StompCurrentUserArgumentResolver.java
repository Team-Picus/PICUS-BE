package com.picus.core.infrastructure.stomp.resolver;

import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.exception.RestApiException;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.security.Principal;
import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._UNAUTHORIZED;

public class StompCurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null
                && String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Principal user = Optional.ofNullable(accessor.getUser())
                .orElseThrow(() -> new RestApiException(_UNAUTHORIZED));

        return Optional.ofNullable(user.getName())
                .orElseThrow(() -> new RestApiException(_UNAUTHORIZED));
    }
}
