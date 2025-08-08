package com.picus.core.post.application.port.in.result;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchPostResult(
    List<PostResult> posts,
    Boolean isLast
) {
    @Builder
    public record PostResult(
        String postNo,
        String thumbnailUrl,
        String authorNickname,
        String title
    ){}

}
