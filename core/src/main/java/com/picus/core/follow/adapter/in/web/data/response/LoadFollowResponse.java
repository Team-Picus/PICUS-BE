package com.picus.core.follow.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadFollowResponse(
        String expertNo,
        LocalDateTime followedAt
) {}
