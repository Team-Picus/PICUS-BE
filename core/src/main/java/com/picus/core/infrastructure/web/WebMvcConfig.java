package com.picus.core.infrastructure.web;

import com.picus.core.infrastructure.security.jwt.ExcludeBlacklistPathProperties;
import com.picus.core.infrastructure.security.jwt.JwtBlacklistInterceptor;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.infrastructure.web.resolver.AccessTokenArgumentResolver;
import com.picus.core.infrastructure.web.resolver.CurrentUserArgumentResolver;
import com.picus.core.infrastructure.web.resolver.RefreshTokenArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;
    private final JwtBlacklistInterceptor jwtBlacklistInterceptor;
    private final ExcludeBlacklistPathProperties excludeBlacklistPathProperties;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(List.of(
                new CurrentUserArgumentResolver(tokenProvider),
                new AccessTokenArgumentResolver(tokenProvider),
                new RefreshTokenArgumentResolver(tokenProvider)
        ));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtBlacklistInterceptor)
                .excludePathPatterns(excludeBlacklistPathProperties.getExcludeAuthPaths());
        }
}
