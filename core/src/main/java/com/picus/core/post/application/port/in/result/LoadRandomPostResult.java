package com.picus.core.post.application.port.in.result;

import lombok.Builder;

@Builder
public record LoadRandomPostResult(
        String postNo,
        String nickname,
        String thumbnailUrl
) {
}
