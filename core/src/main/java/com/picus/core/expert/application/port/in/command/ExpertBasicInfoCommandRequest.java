package com.picus.core.expert.application.port.in.command;

import lombok.Builder;

@Builder
public record ExpertBasicInfoCommandRequest(
        String currentUserNo,
        String profileImageFileKey,
        String backgroundImageFileKey,
        String nickname,
        String link,
        String intro
) {
}
