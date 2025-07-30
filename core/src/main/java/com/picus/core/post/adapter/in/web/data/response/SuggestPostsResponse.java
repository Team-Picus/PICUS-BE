package com.picus.core.post.adapter.in.web.data.response;

import lombok.Builder;

@Builder
public record SuggestPostsResponse(
        String postId,
        String title
) {
}
