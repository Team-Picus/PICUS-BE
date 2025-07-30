package com.picus.core.post.adapter.in.web.data.response;

import lombok.Builder;

@Builder
public record LoadRandomPostResponse(
        String postNo,
        String nickname,
        String thumbnailUrl
) {
}
