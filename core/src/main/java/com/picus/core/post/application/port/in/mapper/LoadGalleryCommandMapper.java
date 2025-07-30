package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import com.picus.core.post.domain.Post;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class LoadGalleryCommandMapper {

    public LoadGalleryResult toResult(Post post, List<String> imageUrls) {
        return LoadGalleryResult.builder()
                .postNo(post.getPostNo())
                .imageUrls(imageUrls)
                .title(post.getTitle())
                .oneLineDescription(post.getOneLineDescription())
                .build();
    }
}
