package com.picus.core.post.adapter.out.persistence.mapper;

import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.domain.PostImage;
import org.springframework.stereotype.Component;

@Component
public class PostImagePersistenceMapper {

    public PostImage toDomain(PostImageEntity postImageEntity) {
        return PostImage.builder()
                .postImageNo(postImageEntity.getPostImageNo())
                .fileKey(postImageEntity.getFileKey())
                .imageOrder(postImageEntity.getImageOrder())
                .build();
    }

    public PostImageEntity toEntity(PostImage postImage) {
        return PostImageEntity.builder()
                .fileKey(postImage.getFileKey())
                .imageOrder(postImage.getImageOrder())
                .build();
    }
}
