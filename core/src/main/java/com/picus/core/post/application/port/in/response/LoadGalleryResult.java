package com.picus.core.post.application.port.in.response;

import lombok.Builder;

@Builder
public record LoadGalleryResult(
        String postNo,
        String thumbnailUrl,
        String title,
        String oneLineDescription
) {
}
