package com.picus.core.post.adapter.out.persistence.mapper;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.domain.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostPersistenceMapper {

    public PostEntity toEntity(Post post) {
        return PostEntity.builder()
                .title(post.getTitle())
                .packageNo(post.getPackageNo())
                .expertNo(post.getAuthorNo())
                .oneLineDescription(post.getOneLineDescription())
                .detailedDescription(post.getDetailedDescription())
                .postThemeTypes(post.getPostThemeTypes())
                .postMoodTypes(post.getPostMoodTypes())
                .spaceType(post.getSpaceType())
                .spaceAddress(post.getSpaceAddress())
                .isPinned(post.getIsPinned())
                .build();
    }
}
