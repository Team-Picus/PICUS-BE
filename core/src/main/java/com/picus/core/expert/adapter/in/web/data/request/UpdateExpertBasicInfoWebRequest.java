package com.picus.core.expert.adapter.in.web.data.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateExpertBasicInfoWebRequest(
        String profileImageFileKey,
        String backgroundImageFileKey,
        String nickname,
        List<String> link,
        String intro
) {
}
