package com.picus.core.post.adapter.out.persistence.mapper;

import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.domain.model.PostImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class PostImagePersistenceMapperTest {

    final PostImagePersistenceMapper mapper = new PostImagePersistenceMapper();

    @Test
    @DisplayName("PostImageEntity -> PostImage 매핑")
    public void toDomain() throws Exception {
        // given
        PostImageEntity entity = PostImageEntity.builder()
                .postImageNo("img-123")
                .fileKey("file_key")
                .imageOrder(1)
                .build();

        // when
        PostImage domain = mapper.toDomain(entity);

        // then
        assertThat(domain.getPostImageNo()).isEqualTo(entity.getPostImageNo());
        assertThat(domain.getFileKey()).isEqualTo(entity.getFileKey());
        assertThat(domain.getImageOrder()).isEqualTo(entity.getImageOrder());
    }

    @Test
    @DisplayName("PostImage -> PostImage Entity 매핑")
    public void toEntity() throws Exception {
        // given
        PostImage domain = PostImage.builder()
                .fileKey("file_key")
                .imageOrder(1)
                .build();

        // when
        PostImageEntity entity = mapper.toEntity(domain);

        // then
        assertThat(entity.getFileKey()).isEqualTo(domain.getFileKey());
        assertThat(entity.getImageOrder()).isEqualTo(domain.getImageOrder());
    }

}