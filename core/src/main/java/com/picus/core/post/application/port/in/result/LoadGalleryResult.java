package com.picus.core.post.application.port.in.result;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadGalleryResult(
        String postNo,
        List<String> imageUrls,
        String title,
        String oneLineDescription
) {
}
