package com.picus.core.post.adapter.in.web.data.response;

import lombok.Builder;

@Builder
public record LoadGalleryResponse(
        String postNo,
        String thumbnailUrl,
        String title,
        String oneLineDescription
) {
}
