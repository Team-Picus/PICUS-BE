package com.picus.core.infrastructure.security.oauth.provider.kakao;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoAttributeConverter {

    public KakaoProfile convert(Map<String, Object> attr) {
        Map<String, Object> account = (Map<String, Object>) attr.get("kakao_account");

        return new KakaoProfile(
                String.valueOf(attr.get("id")),
                (String) account.get("email"),
                (String) account.get("name"),
                (Boolean) account.get("has_phone_number") ? (String) account.get("phone_number") : null,
                attr);
    }
}