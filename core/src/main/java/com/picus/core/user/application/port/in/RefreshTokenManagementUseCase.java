package com.picus.core.user.application.port.in;

import java.time.Duration;

public interface RefreshTokenManagementUseCase {

    void save(String userNo, String refreshToken, Duration duration);
}
