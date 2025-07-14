package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.TokenValidationQuery;
import com.picus.core.user.application.port.out.TokenLifecycleQueryPort;
import com.picus.core.user.config.TokenPrefixProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class TokenValidationService implements TokenValidationQuery {

    private final TokenPrefixProperties tokenPrefixProperties;
    private final TokenLifecycleQueryPort tokenLifecycleQueryPort;

    @Override
    public boolean isBlacklistToken(String token) {
        String key = tokenPrefixProperties.toBlacklist(token);
        return tokenLifecycleQueryPort.existsByKey(key);
    }

    @Override
    public void validate(String userNo, String refreshToken) {
        String key = tokenPrefixProperties.toRefresh(userNo);

        if (!tokenLifecycleQueryPort.existsByKeyAndValue(key, refreshToken)) {
            throw new RestApiException(_NOT_FOUND);
        }
    }

    @Override
    public boolean isWhitelistToken(String token) {
        String key = tokenPrefixProperties.toWhitelist(token);
        return tokenLifecycleQueryPort.existsByKey(key);
    }
}
