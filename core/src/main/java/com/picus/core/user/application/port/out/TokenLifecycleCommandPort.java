package com.picus.core.user.application.port.out;

import java.time.Duration;

public interface TokenLifecycleCommandPort {

    void save(String key, Duration duration);
    void save(String key, String value, Duration duration);
    void delete(String key);
}
