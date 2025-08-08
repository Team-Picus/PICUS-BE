package com.picus.core.post.adapter.out.persistence.mapper;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostPersistenceMapper {

    public Post toDomain(PostEntity postEntity, List<PostImage> postImages) {
        return Post.builder()
                .postNo(postEntity.getPostNo())
                .authorNo(postEntity.getExpertNo())
                .packageNo(postEntity.getPackageNo())
                .title(postEntity.getTitle())
                .oneLineDescription(postEntity.getOneLineDescription())
                .detailedDescription(postEntity.getDetailedDescription())
                .postThemeTypes(postEntity.getPostThemeTypes())
                .snapSubThemes(postEntity.getSnapSubThemes())
                .postMoodTypes(postEntity.getPostMoodTypes())
                .spaceType(postEntity.getSpaceType())
                .spaceAddress(postEntity.getSpaceAddress())
                .isPinned(postEntity.getIsPinned())
                .postImages(postImages)
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .deletedAt(postEntity.getDeletedAt())
                .build();
    }

    public PostEntity toEntity(Post post) {
        return PostEntity.builder()
                .title(post.getTitle())
                .packageNo(post.getPackageNo())
                .expertNo(post.getAuthorNo())
                .oneLineDescription(post.getOneLineDescription())
                .detailedDescription(post.getDetailedDescription())
                .postThemeTypes(post.getPostThemeTypes())
                .snapSubThemes(post.getSnapSubThemes())
                .postMoodTypes(post.getPostMoodTypes())
                .spaceType(post.getSpaceType())
                .spaceAddress(post.getSpaceAddress())
                .isPinned(post.getIsPinned())
                .build();
    }
}
