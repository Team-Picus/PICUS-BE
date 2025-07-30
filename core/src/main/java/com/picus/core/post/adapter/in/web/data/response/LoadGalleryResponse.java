package com.picus.core.post.adapter.in.web.data.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadGalleryResponse(
        String postNo,
        List<String> imageUrls,
        String title,
        String oneLineDescription
) {
}
