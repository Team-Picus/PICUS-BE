package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.LoadGalleryWebResp;
import com.picus.core.post.application.port.in.response.LoadGalleryAppResp;
import org.springframework.stereotype.Component;

@Component
public class LoadGalleryWebMapper {

    public LoadGalleryWebResp toWebResp(LoadGalleryAppResp appResp) {
        return LoadGalleryWebResp.builder()
                .postNo(appResp.postNo())
                .thumbnailUrl(appResp.thumbnailUrl())
                .title(appResp.title())
                .oneLineDescription(appResp.oneLineDescription())
                .build();
    }
}
