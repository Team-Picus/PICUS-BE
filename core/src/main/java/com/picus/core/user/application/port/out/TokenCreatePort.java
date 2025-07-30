package com.picus.core.user.application.port.out;

import java.time.Duration;

public interface TokenCreatePort {

    void create(String key, Duration duration);
    void create(String key, String value, Duration duration);

}
