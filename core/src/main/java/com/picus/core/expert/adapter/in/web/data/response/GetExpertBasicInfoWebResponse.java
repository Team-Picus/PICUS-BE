package com.picus.core.expert.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetExpertBasicInfoWebResponse(
        String activityDuration,
        Integer activityCount,
        LocalDateTime lastActivityAt,
        String intro,
        String backgroundImageUrl,
        String nickname,
        String profileImageUrl
) {
}
