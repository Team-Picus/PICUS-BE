package com.picus.core.infrastructure.security.oauth.handler;

import com.picus.core.infrastructure.security.AppProperties;
import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.infrastructure.security.oauth.principal.SocialPrincipal;
import com.picus.core.infrastructure.security.oauth.repository.OAuth2AuthorizationRequestRepository;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.util.CookieUtil;
import com.picus.core.user.application.port.in.RefreshTokenManagementUseCase;
import com.picus.core.user.application.port.in.SocialAuthenticationUseCase;
import com.picus.core.user.domain.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

import static com.picus.core.shared.exception.code.status.AuthErrorStatus.EXPIRED_REFRESH_TOKEN;
import static com.picus.core.shared.exception.code.status.AuthErrorStatus.INVALID_REDIRECT_URI;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final SocialAuthenticationUseCase socialAuthUseCase;    // port.in
    private final TokenProvider tokenProvider;                   // infra.security.jwt Port
    private final RefreshTokenManagementUseCase refreshTokenManagementUseCase;         // port.out
    private final OAuth2AuthorizationRequestRepository cookieRepository;  // infra.security.oauth.repository
    private final AppProperties appProperties;                      // Infra 공통 설정

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(request);
        clearAuthenticationAttributes(request, response);

        SocialPrincipal principal = (SocialPrincipal) authentication.getPrincipal();

        // 1) 소셜 로그인 → 도메인 User auth / upsert
        User user = socialAuthUseCase
                .authenticate(principal.getProviderId(),
                        principal.getProvider(),
                        principal.getEmail());

        // 2) JWT 발급
        String accessToken = tokenProvider.createAccessToken(user.getUserNo(), user.getRole().name());
        String refreshToken = tokenProvider.createRefreshToken(user.getUserNo(), user.getRole().name());
        Duration duration = tokenProvider.getRemainingDuration(refreshToken)
                .orElseThrow(() -> new RestApiException(EXPIRED_REFRESH_TOKEN));

        // 3) RefreshToken 저장 (Port 호출)
        refreshTokenManagementUseCase.save(user.getUserNo(), refreshToken, duration);

        // 4) Response(쿠키/헤더/JSON) 반영
        CookieUtil.createSecureCookie(response, "refresh_token", refreshToken, (int) duration.getSeconds());
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(HttpServletRequest request) {
        String redirectUri = cookieRepository.removeRedirectUri(request);
        if (redirectUri != null && !appProperties.getOauth2().isAuthorizedRedirectUri(redirectUri)) {
            throw new RestApiException(INVALID_REDIRECT_URI);
        }
        return redirectUri != null
                ? redirectUri
                : appProperties.getOauth2().getDefaultRedirectUri();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieRepository.removeAuthorizationRequestCookies(request, response);
    }
}