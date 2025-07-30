package com.picus.core.expert.application.port.in.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateExpertBasicInfoCommand(
        String currentUserNo,
        String profileImageFileKey,
        String backgroundImageFileKey,
        String nickname,
        List<String> link,
        String intro
) {
}
