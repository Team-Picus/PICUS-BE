package com.picus.core.user.application.port.out;

import java.time.Duration;

public interface TokenCreatePort {

    void save(String key, Duration duration);
    void save(String key, String value, Duration duration);

}
