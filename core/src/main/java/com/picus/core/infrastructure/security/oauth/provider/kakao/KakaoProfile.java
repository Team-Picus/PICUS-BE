package com.picus.core.infrastructure.security.oauth.provider.kakao;

import java.util.Map;

public record KakaoProfile(
        String providerId,
        String email,
        String name,
        String tel,
        Map<String, Object> attributes
) {}