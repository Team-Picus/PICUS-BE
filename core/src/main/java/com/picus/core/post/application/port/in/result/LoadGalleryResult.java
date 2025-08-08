package com.picus.core.post.application.port.in.result;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadGalleryResult(
        String postNo,
        List<PostImageResult> images,
        String title,
        String oneLineDescription
) {
    @Builder
    public record PostImageResult(
            String imageNo,
            String fileKey,
            String imageUrl,
            Integer imageOrder
    ){}
}
