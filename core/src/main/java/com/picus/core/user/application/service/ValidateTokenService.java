package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.ValidateTokenUseCase;
import com.picus.core.user.application.port.out.TokenReadPort;
import com.picus.core.user.config.TokenPrefixProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ValidateTokenService implements ValidateTokenUseCase {

    private final TokenPrefixProperties tokenPrefixProperties;
    private final TokenReadPort tokenReadPort;

    @Override
    public boolean isBlacklistToken(String token) {
        String key = tokenPrefixProperties.toBlacklist(token);
        return tokenReadPort.existsByKey(key);
    }

    @Override
    public boolean isValidRefreshToken(String userNo, String refreshToken) {
        String key = tokenPrefixProperties.toRefresh(userNo);
        return tokenReadPort.existsByKeyAndValue(key, refreshToken);
    }

    @Override
    public boolean isWhitelistToken(String token) {
        String key = tokenPrefixProperties.toWhitelist(token);
        return tokenReadPort.existsByKey(key);
    }
}
