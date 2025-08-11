package com.picus.core.expert.application.port.in.result;

import lombok.Builder;

@Builder
public record SuggestExpertResult(
        String expertNo,
        String nickname,
        String profileImageUrl
) {
}
