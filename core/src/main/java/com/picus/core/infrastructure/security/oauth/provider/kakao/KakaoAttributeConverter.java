package com.picus.core.infrastructure.security.oauth.provider.kakao;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoAttributeConverter {

    public KakaoProfile convert(Map<String, Object> attr) {
        Map<String, Object> account = (Map<String, Object>) attr.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return new KakaoProfile(
                String.valueOf(attr.get("id")),
                (String) account.get("email"),
                attr);
    }
}