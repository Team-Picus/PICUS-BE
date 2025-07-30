package com.picus.core.moodboard.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadMoodboardResponse(
        String userNo,
        String postNo,
        LocalDateTime createdAt
) {}
