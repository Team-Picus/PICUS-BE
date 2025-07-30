package com.picus.core.user.application.service;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.IssueTokenUseCase;
import com.picus.core.user.application.port.in.result.IssueTokenResult;
import com.picus.core.user.application.port.out.TokenCreatePort;
import com.picus.core.user.config.TokenPrefixProperties;
import com.picus.core.user.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.picus.core.shared.exception.code.status.AuthErrorStatus.EXPIRED_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class IssueTokenService implements IssueTokenUseCase {

    private final TokenProvider tokenProvider;
    private final TokenPrefixProperties tokenPrefixProperties;
    private final TokenCreatePort tokenCreatePort;

    @Override
    public IssueTokenResult issue(String userNo, Role role) {
        String accessToken = tokenProvider.createAccessToken(userNo, role.name());
        String refreshToken = tokenProvider.createRefreshToken(userNo, role.name());

        Duration duration = tokenProvider.getRemainingDuration(refreshToken)
                .orElseThrow(() -> new RestApiException(EXPIRED_REFRESH_TOKEN));

        String key = tokenPrefixProperties.toWhitelist(refreshToken);
        tokenCreatePort.save(key, duration);

        return IssueTokenResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .duration(duration)
                .build();
    }
}
