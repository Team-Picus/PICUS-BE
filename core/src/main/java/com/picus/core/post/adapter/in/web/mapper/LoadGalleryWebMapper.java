package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadGalleryResponse;
import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import org.springframework.stereotype.Component;

@Component
public class LoadGalleryWebMapper {

    public LoadGalleryResponse toResponse(LoadGalleryResult appResp) {
        return LoadGalleryResponse.builder()
                .postNo(appResp.postNo())
                .imageUrls(appResp.imageUrls())
                .title(appResp.title())
                .oneLineDescription(appResp.oneLineDescription())
                .build();
    }
}
