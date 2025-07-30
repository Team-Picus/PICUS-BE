package com.picus.core.user.application.port.in;

import java.time.Duration;

public interface WhitelistTokenUseCase {

    void whitelist(String token, Duration duration);

}
