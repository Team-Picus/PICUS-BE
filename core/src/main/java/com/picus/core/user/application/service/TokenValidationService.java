package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.TokenValidationUseCase;
import com.picus.core.user.application.port.out.TokenLifecycleCommandPort;
import com.picus.core.user.application.port.out.TokenLifecycleQueryPort;
import com.picus.core.user.config.TokenPrefixProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@UseCase
@Transactional
@RequiredArgsConstructor
public class TokenValidationService implements TokenValidationUseCase {

    private final TokenPrefixProperties tokenPrefixProperties;
    private final TokenLifecycleQueryPort tokenLifecycleQueryPort;
    private final TokenLifecycleCommandPort tokenLifecycleCommandPort;

    @Override
    public boolean isBlacklistToken(String token) {
        String key = tokenPrefixProperties.toBlacklist(token);
        return tokenLifecycleQueryPort.existsByKey(key);
    }

    @Override
    public boolean isWhitelistToken(String token) {
        String key = tokenPrefixProperties.toWhitelist(token);
        return tokenLifecycleQueryPort.existsByKey(key);
    }

    @Override
    public void blacklist(String token, Duration duration) {
        String key = tokenPrefixProperties.toBlacklist(token);
        tokenLifecycleCommandPort.save(key, duration);
    }

    @Override
    public void whitelist(String token, Duration duration) {
        String key = tokenPrefixProperties.toWhitelist(token);
        tokenLifecycleCommandPort.save(key, duration);
    }
}
