package com.picus.core.post.application.port.in.result;

import lombok.Builder;

@Builder
public record LoadGalleryResult(
        String postNo,
        String thumbnailUrl,
        String title,
        String oneLineDescription
) {
}
