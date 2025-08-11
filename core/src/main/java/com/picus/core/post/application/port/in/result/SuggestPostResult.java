package com.picus.core.post.application.port.in.result;

import lombok.Builder;

@Builder
public record SuggestPostResult(
        String postNo,
        String title
) {
}
