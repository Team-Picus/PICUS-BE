package com.picus.core.global.config.resolver;

import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.common.exception.code.status.GlobalErrorStatus;
import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CommonPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CommonPrincipal.class) != null
                && UserPrincipal.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hello = authentication.getPrincipal() instanceof UserPrincipal;
//        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }
        return authentication.getPrincipal();
    }
}
