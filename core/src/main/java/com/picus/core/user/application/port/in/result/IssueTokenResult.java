package com.picus.core.user.application.port.in.result;

import lombok.Builder;

import java.time.Duration;

@Builder
public record IssueTokenResult(
        String accessToken,
        String refreshToken,
        Duration duration
) {
}
