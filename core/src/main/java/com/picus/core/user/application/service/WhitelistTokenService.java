package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.WhitelistTokenUseCase;
import com.picus.core.user.application.port.out.TokenCreatePort;
import com.picus.core.user.config.TokenPrefixProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@UseCase
@Transactional
@RequiredArgsConstructor
public class WhitelistTokenService implements WhitelistTokenUseCase {

    private final TokenPrefixProperties tokenPrefixProperties;
    private final TokenCreatePort tokenCreatePort;

    @Override
    public void whitelist(String token, Duration duration) {
        String key = tokenPrefixProperties.toWhitelist(token);
        tokenCreatePort.save(key, duration);
    }
}
