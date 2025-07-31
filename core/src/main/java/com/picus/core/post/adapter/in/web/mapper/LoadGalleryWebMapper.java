package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadGalleryResponse;
import com.picus.core.post.adapter.in.web.data.response.LoadGalleryResponse.PostImageResponse;
import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.picus.core.post.application.port.in.result.LoadGalleryResult.*;

@Component
public class LoadGalleryWebMapper {

    public LoadGalleryResponse toResponse(LoadGalleryResult appResp) {
        List<PostImageResult> images = appResp.images();
        return LoadGalleryResponse.builder()
                .postNo(appResp.postNo())
                .images(toPostImageResponses(images))
                .title(appResp.title())
                .oneLineDescription(appResp.oneLineDescription())
                .build();
    }

    private List<PostImageResponse> toPostImageResponses(List<PostImageResult> images) {
        return images.stream()
                .map(postImageResult -> PostImageResponse
                        .builder()
                        .imageNo(postImageResult.imageNo())
                        .fileKey(postImageResult.fileKey())
                        .imageUrl(postImageResult.imageUrl())
                        .imageOrder(postImageResult.imageOrder())
                        .build()
                ).toList();
    }
}
