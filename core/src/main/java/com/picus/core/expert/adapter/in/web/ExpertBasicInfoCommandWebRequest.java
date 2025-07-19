package com.picus.core.expert.adapter.in.web;

import lombok.Builder;

@Builder
public record ExpertBasicInfoCommandWebRequest(
        String profileImageFileKey,
        String backgroundImageFileKey,
        String nickname,
        String link,
        String intro
) {
}
