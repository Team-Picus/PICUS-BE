package com.picus.core.global.config;

import com.picus.core.domain.post.infra.helper.ViewHistoryCookieHelper;
import com.picus.core.global.config.resolver.*;
import com.picus.core.global.config.security.path.ExcludeAuthPathProperties;
import com.picus.core.global.jwt.TokenProvider;
import com.picus.core.global.oauth.interceptor.JwtBlacklistInterceptor;
import com.picus.core.global.ratelimiter.RateLimitInterceptor;
import com.picus.core.global.ratelimiter.path.ExcludeRateLimitPathProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ViewHistoryCookieHelper viewHistoryCookieHelper;
    private final TokenProvider tokenProvider;
    private final JwtBlacklistInterceptor jwtBlacklistInterceptor;
    private final ExcludeAuthPathProperties excludeAuthPathProperties;
    private final RateLimitInterceptor rateLimitInterceptor;
    private final ExcludeRateLimitPathProperties excludeRateLimitPathProperties;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(
                List.of(new ViewHistoryArgumentResolver(viewHistoryCookieHelper),
                        new CommonPrincipalArgumentResolver(),
                        new ExpertPrincipalArgumentResolver(),
                        new RefreshTokenArgumentResolver(tokenProvider),
                        new AccessTokenArgumentResolver(tokenProvider)
                )
        );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtBlacklistInterceptor)
                .excludePathPatterns(excludeAuthPathProperties.getExcludeAuthPaths());

        registry.addInterceptor(rateLimitInterceptor)
                .excludePathPatterns(excludeRateLimitPathProperties.getExcludeAuthPaths());
    }
}
