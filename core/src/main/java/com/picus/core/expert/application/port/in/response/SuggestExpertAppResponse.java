package com.picus.core.expert.application.port.in.response;

import lombok.Builder;

@Builder
public record SuggestExpertAppResponse(
        String expertNo,
        String nickname,
        String profileImageUrl
) {
}
