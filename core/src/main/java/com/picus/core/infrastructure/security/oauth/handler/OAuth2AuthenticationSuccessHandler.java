package com.picus.core.infrastructure.security.oauth.handler;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.infrastructure.security.oauth.principal.SocialPrincipal;
import com.picus.core.infrastructure.security.oauth.repository.OAuth2AuthorizationRequestRepository;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.util.CookieUtil;
import com.picus.core.user.application.port.in.TokenManagementCommandPort;
import com.picus.core.user.application.port.in.SocialAuthenticationUseCase;
import com.picus.core.user.application.port.in.command.InitSocialUserCommand;
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

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final SocialAuthenticationUseCase socialAuthUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManagementCommandPort tokenManagementCommandPort;
    private final OAuth2AuthorizationRequestRepository cookieRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        clearAuthenticationAttributes(request, response);

        SocialPrincipal principal = (SocialPrincipal) authentication.getPrincipal();

        // 1) 소셜 로그인 → 도메인 User auth / upsert
        User user = socialAuthUseCase
                .authenticate(
                        new InitSocialUserCommand(
                                principal.getProviderId(),
                                principal.getProvider(),
                                principal.getEmail(),
                                principal.getName(),
                                principal.getTel()
                        )
                );

        // 2) JWT 발급
        String accessToken = tokenProvider.createAccessToken(user.getUserNo(), user.getRole().name());
        String refreshToken = tokenProvider.createRefreshToken(user.getUserNo(), user.getRole().name());
        Duration duration = tokenProvider.getRemainingDuration(refreshToken)
                .orElseThrow(() -> new RestApiException(EXPIRED_REFRESH_TOKEN));

        // 3) RefreshToken 저장 (Port 호출)
        tokenManagementCommandPort.refreshToken(user.getUserNo(), refreshToken, duration);

        // 4) Response(쿠키/헤더/JSON) 반영
        CookieUtil.createSecureCookie(response, "refresh_token", refreshToken, (int) duration.getSeconds());
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        response.setContentType("application/json");
        String body = BaseResponse.onSuccess().toString();
        response.getWriter().write(body);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieRepository.removeAuthorizationRequestCookies(request, response);
    }
}