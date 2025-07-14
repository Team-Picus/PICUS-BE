package com.picus.core.user.application.port.in;

public interface TokenValidationQuery {

    boolean isWhitelistToken(String token);
    boolean isBlacklistToken(String token);
    void validate(String userNo, String refreshToken);

}
