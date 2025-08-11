package com.picus.core.weekly_magazine.adapter.in.web.data.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadWeeklyMagazinePostByWeekAtResponse(
    List<PostResponse> posts
) {
    @Builder
    public record PostResponse(
            String postNo,
            String authorNickname,
            String postTitle,
            String thumbnailUrl
    ){}
}
