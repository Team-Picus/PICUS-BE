package com.picus.core.domain.post.resolver;

import com.picus.core.domain.post.helper.ViewHistoryCookieHelper;
import com.picus.core.domain.post.resolver.annotation.CheckViewHistory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@RequiredArgsConstructor
public class ViewHistoryArgumentResolver implements HandlerMethodArgumentResolver {

    private final ViewHistoryCookieHelper viewHistoryCookieHelper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CheckViewHistory.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();

        Long postId = extractPostId(request);
        boolean alreadyViewed = viewHistoryCookieHelper.hasAlreadyViewed(postId, request);

        if (!alreadyViewed) {
            viewHistoryCookieHelper.bindViewCookie(postId, request, response);
        }

        return !alreadyViewed;
    }

    private Long extractPostId(HttpServletRequest request) {
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null && pathVariables.containsKey("postId")) {
            try {
                return Long.valueOf(pathVariables.get("postId"));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

}
