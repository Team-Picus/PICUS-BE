package com.picus.core.global.jwt;

import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.config.security.path.ExcludeAuthPathProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.io.PrintWriter;

import static com.picus.core.global.common.exception.code.status.AuthErrorStatus.EMPTY_JWT;
import static com.picus.core.global.common.exception.code.status.AuthErrorStatus.INVALID_ACCESS_TOKEN;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ExcludeAuthPathProperties excludeAuthPathProperties;

    private static final PathPatternParser pathPatternParser = new PathPatternParser();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isExcludedPath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            StringBuffer url = request.getRequestURL();

            tokenProvider.getToken(request).ifPresentOrElse(token -> {
                    if (tokenProvider.validateToken(token))
                        setAuthentication(token);
                    else
                        throw new RestApiException(INVALID_ACCESS_TOKEN);
                }, () -> {
                    throw new RestApiException(EMPTY_JWT);
                }
            );

            filterChain.doFilter(request, response);
        } catch (RestApiException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
//            System.out.println(e.getMessage());
            writer.flush();
            writer.close();
        }
    }

    public boolean isExcludedPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());

        return excludeAuthPathProperties.getPaths().stream()
                .anyMatch(authPath ->
                        pathPatternParser.parse(authPath.getPathPattern())
                                .matches(PathContainer.parsePath(requestPath))
                        && requestMethod.equals(HttpMethod.valueOf(authPath.getMethod()))
                );
    }

    private void setAuthentication(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
