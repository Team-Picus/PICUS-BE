package com.picus.core.global.jwt;

import lombok.Getter;

@Getter
public enum RedisTokenPathPrefix {
    ACCESS_TOKEN("BLACKLIST:ACCESS:"),
    REFRESH_TOKEN("BLACKLIST:REFRESH:");

    private final String prefix;

    RedisTokenPathPrefix(String prefix) {
        this.prefix = prefix;
    }
}
