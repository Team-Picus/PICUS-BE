package com.picus.core.post.application.port.in.response;

import lombok.Builder;

@Builder
public record SuggestPostsResult(
        String postNo,
        String title
) {
}
