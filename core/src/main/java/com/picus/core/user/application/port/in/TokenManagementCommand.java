package com.picus.core.user.application.port.in;

import com.picus.core.user.domain.model.Role;

import java.time.Duration;

public interface TokenManagementCommand {

    void whitelist(String token, Duration duration);
    void blacklist(String userNo, String token);
    void refreshToken(String userNo, String refreshToken, Duration duration);
    String reissue(String userNo, Role role);

}
