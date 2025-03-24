package com.picus.core.domain.user.application.dto.response;

public record AuthTokenRes(
        String accessToken,
        String refreshToken
) {
}
