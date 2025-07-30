package com.picus.core.infrastructure.security.oauth.handler;

import com.picus.core.infrastructure.security.oauth.principal.SocialPrincipal;
import com.picus.core.infrastructure.security.oauth.repository.OAuth2AuthorizationRequestRepository;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.shared.util.CookieUtil;
import com.picus.core.user.application.port.in.IssueTokenUseCase;
import com.picus.core.user.application.port.in.SocialAuthenticationUseCase;
import com.picus.core.user.application.port.in.command.InitSocialUserCommand;
import com.picus.core.user.application.port.in.result.IssueTokenResult;
import com.picus.core.user.domain.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final SocialAuthenticationUseCase socialAuthUseCase;
    private final OAuth2AuthorizationRequestRepository cookieRepository;
    private final IssueTokenUseCase issueTokenUseCase;

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
        IssueTokenResult result = issueTokenUseCase.issue(user.getUserNo(), user.getRole());

        // 4) Response(쿠키/헤더/JSON) 반영
        CookieUtil.createSecureCookie(response, "refresh_token", result.refreshToken(), (int) result.duration().getSeconds());
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + result.accessToken());

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