package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.response.LoadGalleryAppResp;
import com.picus.core.post.domain.Post;
import org.springframework.stereotype.Component;


@Component
public class LoadGalleryAppMapper {

    public LoadGalleryAppResp toAppResp(Post post, String thumbnailUrl) {
        return LoadGalleryAppResp.builder()
                .postNo(post.getPostNo())
                .thumbnailUrl(thumbnailUrl)
                .title(post.getTitle())
                .oneLineDescription(post.getOneLineDescription())
                .build();
    }
}
