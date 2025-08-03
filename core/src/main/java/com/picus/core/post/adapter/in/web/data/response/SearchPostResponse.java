package com.picus.core.post.adapter.in.web.data.response;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchPostResponse(
    List<PostResponse> posts,
    Boolean isLast
) {
    @Builder
    public record PostResponse (
        String postNo,
        String thumbnailUrl,
        String authorNickname,
        String title
    ){}

}
