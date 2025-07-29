package com.picus.core.post.application.port.in.response;

import lombok.Builder;

@Builder
public record SuggestPostsAppResp(
        String postNo,
        String title
) {
}
