package com.picus.core.expert.adapter.in.web.data.response;

import lombok.Builder;

@Builder
public record SearchExpertResponse(
        String expertNo,
        String nickname,
        String profileImageUrl
) {
}
