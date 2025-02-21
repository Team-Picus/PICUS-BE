package com.picus.core.global;

import com.picus.core.domain.post.helper.ViewHistoryCookieHelper;
import com.picus.core.domain.post.resolver.ViewHistoryArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ViewHistoryCookieHelper viewHistoryCookieHelper;

    public WebMvcConfig(ViewHistoryCookieHelper viewHistoryCookieHelper) {
        this.viewHistoryCookieHelper = viewHistoryCookieHelper;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ViewHistoryArgumentResolver(viewHistoryCookieHelper));
    }
}
