package com.picus.core.user.application.port.in;

public interface ValidateTokenUseCase {

    boolean isWhitelistToken(String token);
    boolean isBlacklistToken(String token);
    boolean isValidRefreshToken(String userNo, String refreshToken);

}
