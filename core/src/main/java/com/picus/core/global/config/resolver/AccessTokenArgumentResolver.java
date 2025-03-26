package com.picus.core.global.config.resolver;

import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.config.resolver.annotation.AccessToken;
import com.picus.core.global.config.resolver.annotation.RefreshToken;
import com.picus.core.global.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.picus.core.global.common.exception.code.status.AuthErrorStatus.EMPTY_JWT;
import static com.picus.core.global.common.exception.code.status.AuthErrorStatus.UNSUPPORTED_JWT;
import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._UNAUTHORIZED;

@RequiredArgsConstructor
public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AccessToken.class) != null
                && String.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new RestApiException(_UNAUTHORIZED);
        }

        String accessToken = tokenProvider.getToken(request)
                .orElseThrow(() -> new RestApiException(_UNAUTHORIZED));

        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new RestApiException(EMPTY_JWT);
        }

        if(!tokenProvider.isAccessToken(accessToken)) {
           throw new RestApiException(UNSUPPORTED_JWT);
        }

        return accessToken;
    }
}
