package com.picus.core.user.application.service;

import com.picus.core.infrastructure.security.jwt.TokenProvider;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.LogoutUseCase;
import com.picus.core.user.application.port.out.TokenCreatePort;
import com.picus.core.user.application.port.out.TokenDeletePort;
import com.picus.core.user.config.TokenPrefixProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.picus.core.shared.exception.code.status.AuthErrorStatus.EXPIRED_MEMBER_JWT;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    private final TokenDeletePort tokenDeletePort;
    private final TokenProvider tokenProvider;
    private final TokenPrefixProperties tokenPrefixProperties;
    private final TokenCreatePort tokenCreatePort;

    @Override
    public void logout(String userNo, String token) {
        String key = tokenPrefixProperties.toBlacklist(token);

        Duration duration = tokenProvider.getRemainingDuration(token)
                .orElseThrow(() -> new RestApiException(EXPIRED_MEMBER_JWT));

        tokenCreatePort.save(key, duration);
        tokenDeletePort.delete(tokenPrefixProperties.toRefresh(userNo));
    }
}
