package com.picus.core.expert.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateExpertBasicInfoAppRequest(
        String currentUserNo,
        String profileImageFileKey,
        String backgroundImageFileKey,
        String nickname,
        List<String> link,
        String intro
) {
}
