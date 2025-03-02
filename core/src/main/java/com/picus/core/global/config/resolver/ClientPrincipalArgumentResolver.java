package com.picus.core.global.config.resolver;

import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.common.exception.code.status.GlobalErrorStatus;
import com.picus.core.global.config.resolver.annotation.ClientPrincipal;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.file.AccessDeniedException;

public class ClientPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(ClientPrincipal.class) != null
                && UserPrincipal.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (!"CLIENT".equals(principal.getUserType().toString())) {
            throw new RestApiException(GlobalErrorStatus._CLIENT_ONLY_ERROR);
        }
        return principal;
    }
}
