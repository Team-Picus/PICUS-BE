package com.picus.core.expert.application.port.in.response;

import lombok.Builder;

@Builder
public record SearchExpertResponse(
        String expertNo,
        String nickname,
        String profileImageUrl
) {
}
