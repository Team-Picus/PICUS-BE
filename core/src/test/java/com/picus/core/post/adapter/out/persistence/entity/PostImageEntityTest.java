package com.picus.core.post.adapter.out.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostImageEntityTest {

    @Test
    @DisplayName("PostImageEntity의 ImageOrder를 할당 한다.")
    public void assignImageOrder() throws Exception {
        // given
        PostImageEntity postImageEntity = createPostImageEntity("file.jpg", 1, new PostEntity());

        // when
        postImageEntity.assignImageOrder(2);

        // then
        assertThat(postImageEntity.getImageOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("PostImageEntity에 PostEntity를 바인딩한다.")
    public void bindPostEntity() throws Exception {
        // given
        PostImageEntity postImageEntity = createPostImageEntity("file.jpg", 1, null);

        PostEntity postEntity = PostEntity.builder().build();
        // when
        postImageEntity.bindPostEntity(postEntity);

        // then
        assertThat(postImageEntity.getPostEntity()).isEqualTo(postEntity);
    }

    @Test
    @DisplayName("PostImageEntity를 업데이트 한다.")
    public void updatePostEntity() throws Exception {
        // given
        PostImageEntity postImageEntity = createPostImageEntity("old.jpg", 1, new PostEntity());

        // when
        postImageEntity.updatePostImageEntity("new.jpg", 2);

        // then
        assertThat(postImageEntity.getFileKey()).isEqualTo("new.jpg");
        assertThat(postImageEntity.getImageOrder()).isEqualTo(2);
    }

    private PostImageEntity createPostImageEntity(String fileKey, int imageOrder, PostEntity postEntity) {
        return PostImageEntity.builder()
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .postEntity(postEntity)
                .build();
    }
}