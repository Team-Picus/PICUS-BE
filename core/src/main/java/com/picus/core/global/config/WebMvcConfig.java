package com.picus.core.global.config;

import com.picus.core.domain.post.infra.helper.ViewHistoryCookieHelper;
import com.picus.core.global.config.resolver.*;
import com.picus.core.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ViewHistoryCookieHelper viewHistoryCookieHelper;
    private final TokenProvider tokenProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(
                List.of(new ViewHistoryArgumentResolver(viewHistoryCookieHelper),
                        new CommonPrincipalArgumentResolver(),
                        new ExpertPrincipalArgumentResolver(),
                        new RefreshTokenArgumentResolver(tokenProvider)
                )
        );
    }
}
