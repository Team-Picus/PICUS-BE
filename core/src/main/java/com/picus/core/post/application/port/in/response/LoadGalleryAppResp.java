package com.picus.core.post.application.port.in.response;

import lombok.Builder;

@Builder
public record LoadGalleryAppResp(
        String postNo,
        String thumbnailUrl,
        String title,
        String oneLineDescription
) {
}
