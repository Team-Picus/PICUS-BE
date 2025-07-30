package com.picus.core.post.application.port.in.result;

import lombok.Builder;

@Builder
public record SuggestPostsResult(
        String postNo,
        String title
) {
}
