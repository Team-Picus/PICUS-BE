package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadGalleryWebResp;
import com.picus.core.post.application.port.in.response.LoadGalleryResult;
import org.springframework.stereotype.Component;

@Component
public class LoadGalleryWebMapper {

    public LoadGalleryWebResp toWebResp(LoadGalleryResult appResp) {
        return LoadGalleryWebResp.builder()
                .postNo(appResp.postNo())
                .thumbnailUrl(appResp.thumbnailUrl())
                .title(appResp.title())
                .oneLineDescription(appResp.oneLineDescription())
                .build();
    }
}
