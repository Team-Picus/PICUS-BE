package com.picus.core.expert.adapter.in.web.data.response;

import lombok.Builder;

@Builder
public record SearchExpertWebResponse(
        String expertNo,
        String nickname,
        String profileImageUrl
) {
}
