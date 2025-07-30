package com.picus.core.user.application.port.out;

public interface TokenReadPort {

    boolean existsByKeyAndValue(String key, String value);
    boolean existsByKey(String key);

}
