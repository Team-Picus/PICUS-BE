package com.picus.core.expert.application.port.in.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ExpertBasicInfoResult(
        String expertNo,
        String activityDuration,
        Integer activityCount,
        LocalDateTime lastActivityAt,
        String intro,
        String backgroundImageUrl,
        String nickname,
        String profileImageUrl,
        List<String> links
) {
}
