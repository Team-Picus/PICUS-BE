package com.picus.core.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token.prefix")
public record TokenPrefixProperties (
        String access,
        String refresh,
        String blacklist,
        String whitelist
){
    public String toWhitelist(String token) {
        return whitelist + token;
    }

    public String toBlacklist(String token) {
        return blacklist + token;
    }

    public String toRefresh(String userNo) {
        return refresh + userNo;
    }
}