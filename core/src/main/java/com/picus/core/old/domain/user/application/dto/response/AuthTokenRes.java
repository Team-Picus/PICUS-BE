package com.picus.core.old.domain.user.application.dto.response;

public record AuthTokenRes(
        String accessToken,
        String refreshToken
) {
}
