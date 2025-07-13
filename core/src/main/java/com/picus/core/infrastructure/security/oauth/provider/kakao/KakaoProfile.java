package com.picus.core.infrastructure.security.oauth.provider.kakao;

import java.util.Map;

public record KakaoProfile(
        String providerId,
        String email,
        Map<String, Object> attributes
) {}