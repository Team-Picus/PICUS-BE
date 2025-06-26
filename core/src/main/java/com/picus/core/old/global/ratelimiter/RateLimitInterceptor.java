package com.picus.core.old.global.ratelimiter;

import com.picus.core.old.global.common.exception.RestApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus._TOO_MANY_REQUEST;

@Setter
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RequestRateLimiter requestRateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userNoStr = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!requestRateLimiter.isRequestAllowed(userNoStr, request.getRequestURI()))
            throw new RestApiException(_TOO_MANY_REQUEST);

        return true;
    }
}
