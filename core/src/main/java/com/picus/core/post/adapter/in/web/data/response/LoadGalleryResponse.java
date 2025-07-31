package com.picus.core.post.adapter.in.web.data.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadGalleryResponse(
        String postNo,
        List<PostImageResponse> images,
        String title,
        String oneLineDescription
) {
    @Builder
    public record PostImageResponse(
            String imageNo,
            String fileKey,
            String imageUrl,
            Integer imageOrder
    ) {
    }
}
