package com.picus.core.expert.application.port.in.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetExpertBasicInfoAppResponse(
        String activityDuration,
        Integer activityCount,
        LocalDateTime lastActivityAt,
        String intro,
        String backgroundImageUrl,
        String nickname,
        String profileImageUrl
) {
}
