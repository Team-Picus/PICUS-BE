package com.picus.core.post.adapter.in.web.data.request;

import lombok.Builder;

@Builder
public record CreateCommentRequest(
        String content
) {
}
