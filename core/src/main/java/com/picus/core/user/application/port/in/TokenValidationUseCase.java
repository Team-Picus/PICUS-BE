package com.picus.core.user.application.port.in;

import java.time.Duration;

public interface TokenValidationUseCase {

    // query
    boolean isBlacklistToken(String token);
    boolean isWhitelistToken(String token);

    // command
    void whitelist(String token, Duration duration);
    void blacklist(String token, Duration duration);
}
