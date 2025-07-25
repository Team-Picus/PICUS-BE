package com.picus.core.user.adapter.in.web.data.response;

import lombok.Builder;

@Builder
public record GetProfileResponse(
        String nickname,
        String email,
        String fileKey,
        Integer reservationHistoryCount,
        Integer followCount,
        Integer myMoodboardCount
) {}
