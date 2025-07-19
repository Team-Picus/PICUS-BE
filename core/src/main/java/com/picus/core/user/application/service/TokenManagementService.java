package com.picus.core.user.application.service;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.TokenManagementCommandPort;
import com.picus.core.user.application.port.out.TokenLifecycleCommandPort;
import com.picus.core.user.config.TokenPrefixProperties;
import com.picus.core.user.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.picus.core.shared.exception.code.status.AuthErrorStatus.EXPIRED_MEMBER_JWT;

@UseCase
@Transactional
@RequiredArgsConstructor
public class TokenManagementService implements TokenManagementCommandPort {

    private final TokenLifecycleCommandPort tokenLifecycleCommandPort;
    private final TokenProvider tokenProvider;
    private final TokenPrefixProperties tokenPrefixProperties;

    @Override
    public void whitelist(String token, Duration duration) {
        String key = tokenPrefixProperties.toWhitelist(token);
        tokenLifecycleCommandPort.save(key, duration);
    }

    @Override
    public void blacklist(String userNo, String token) {
        String key = tokenPrefixProperties.toBlacklist(token);

        Duration duration = tokenProvider.getRemainingDuration(token)
                .orElseThrow(() -> new RestApiException(EXPIRED_MEMBER_JWT));

        tokenLifecycleCommandPort.save(key, duration);
        tokenLifecycleCommandPort.delete(tokenPrefixProperties.toRefresh(userNo));
    }

    @Override
    public void refreshToken(String userNo, String refreshToken, Duration duration) {
        String key = tokenPrefixProperties.toRefresh(userNo);
        tokenLifecycleCommandPort.save(key, refreshToken, duration);
    }

    @Override
    public String reissue(String userNo, Role role) {
        return tokenProvider.createAccessToken(userNo, role.name());
    }
}
