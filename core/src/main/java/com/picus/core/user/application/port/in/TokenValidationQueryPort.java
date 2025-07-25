package com.picus.core.user.application.port.in;

public interface TokenValidationQueryPort {

    boolean isWhitelistToken(String token);
    boolean isBlacklistToken(String token);
    void validate(String userNo, String refreshToken);

}
