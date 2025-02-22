package com.picus.core.domain.post.resolver;

import com.picus.core.domain.post.infra.helper.ViewHistoryCookieHelper;
import com.picus.core.global.config.resolver.ViewHistoryArgumentResolver;
import com.picus.core.global.config.resolver.annotation.CheckViewHistory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewHistoryArgumentResolverTest {

    @Mock
    private ViewHistoryCookieHelper viewHistoryCookieHelper;

    @Mock
    private MethodParameter methodParameter;

    @Mock
    private NativeWebRequest nativeWebRequest;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private WebDataBinderFactory binderFactory;

    @Mock
    private ModelAndViewContainer mavContainer;

    private ViewHistoryArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resolver = new ViewHistoryArgumentResolver(viewHistoryCookieHelper);
        when(nativeWebRequest.getNativeRequest()).thenReturn(httpServletRequest);
        when(nativeWebRequest.getNativeResponse()).thenReturn(httpServletResponse);
    }

    @Test
    void supportsParameter_true() {
        // given
        when(methodParameter.hasParameterAnnotation(CheckViewHistory.class)).thenReturn(true);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertTrue(result);
    }

    @Test
    void supportsParameter_false() {
        // given
        when(methodParameter.hasParameterAnnotation(CheckViewHistory.class)).thenReturn(false);

        // when
        boolean result = resolver.supportsParameter(methodParameter);

        // then
        assertFalse(result);
    }

    @Test
    void resolveArgument_false() throws Exception {
        // given
        Long postId = 1L;
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("postId", postId.toString());
        when(httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                .thenReturn(pathVariables);

        when(viewHistoryCookieHelper.hasAlreadyViewed(postId, httpServletRequest)).thenReturn(true);

        // when
        Object result = resolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);

        // then
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        verify(viewHistoryCookieHelper, never()).bindViewCookie(anyLong(), any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @Test
    void resolveArgument_true() throws Exception {
        // given
        Long postId = 2L;
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("postId", postId.toString());
        when(httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                .thenReturn(pathVariables);

        when(viewHistoryCookieHelper.hasAlreadyViewed(postId, httpServletRequest)).thenReturn(false);

        // when
        Object result = resolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);

        // then
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        verify(viewHistoryCookieHelper, times(1)).bindViewCookie(postId, httpServletRequest, httpServletResponse);
    }
}
