package com.picus.core.expert.application.port.in.result;

import lombok.Builder;

@Builder
public record SearchExpertResult(
        String expertNo,
        String nickname,
        String profileImageUrl
) {
}
