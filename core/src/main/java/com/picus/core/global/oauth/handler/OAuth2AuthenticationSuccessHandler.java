package com.picus.core.global.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.domain.user.application.dto.response.AuthTokenRes;
import com.picus.core.domain.user.application.usecase.TokenBlacklistUseCase;
import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.config.properties.AppProperties;
import com.picus.core.global.jwt.TokenProvider;
import com.picus.core.global.oauth.entity.RefreshToken;
import com.picus.core.global.oauth.entity.UserPrincipal;
import com.picus.core.global.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.picus.core.global.oauth.repository.RefreshTokenRepository;
import com.picus.core.global.utils.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import static com.picus.core.global.common.exception.code.status.AuthErrorStatus.FAILED_SOCIAL_LOGIN;
import static com.picus.core.global.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final RefreshTokenRepository userRefreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // redirectUri 가져오기
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // todo: 추후에 허용할 redirect url를 설정할 것
//        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
        if (redirectUri.isPresent()) {
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        // todo: 토큰이 만료 되기 전 소셜 로그인을 다시 할 시 Active한 토큰이 생김.
//        tokenBlacklistUseCase.invalidateTokens();

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AuthTokenRes authTokenRes = tokenProvider.createToken(userPrincipal.getUserId(), userPrincipal.getUserType());

        userRefreshTokenRepository.saveAndFlush(new RefreshToken(userPrincipal.getUserId(), authTokenRes.refreshToken()));

        sendAuthTokenResponse(response, authTokenRes);

        return targetUrl;
    }

    private static void sendAuthTokenResponse(HttpServletResponse response, AuthTokenRes authTokenRes) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(authTokenRes);
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RestApiException(FAILED_SOCIAL_LOGIN);
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
