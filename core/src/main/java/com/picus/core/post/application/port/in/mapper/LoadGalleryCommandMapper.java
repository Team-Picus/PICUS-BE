package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import com.picus.core.post.domain.Post;
import org.springframework.stereotype.Component;


@Component
public class LoadGalleryCommandMapper {

    public LoadGalleryResult toAppResp(Post post, String thumbnailUrl) {
        return LoadGalleryResult.builder()
                .postNo(post.getPostNo())
                .thumbnailUrl(thumbnailUrl)
                .title(post.getTitle())
                .oneLineDescription(post.getOneLineDescription())
                .build();
    }
}
