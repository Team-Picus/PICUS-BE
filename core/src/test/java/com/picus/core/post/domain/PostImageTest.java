package com.picus.core.post.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostImageTest {

    @Test
    @DisplayName("PostImage를 업데이트한다.")
    public void updateImage_success() throws Exception {
        // given
        PostImage postImage = PostImage.builder()
                .fileKey("old.jpg")
                .imageOrder(2)
                .build();
        // when
        postImage.updatePostImage("new.jpg", 1);

        // then
        assertThat(postImage.getFileKey()).isEqualTo("new.jpg");
        assertThat(postImage.getImageOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("PostImage를 업데이트할 때 null이 들어오면 수정되지 않는다.")
    public void updateImage_null() throws Exception {
        // given
        PostImage postImage = PostImage.builder()
                .fileKey("old.jpg")
                .imageOrder(2)
                .build();
        // when
        postImage.updatePostImage(null, null);

        // then
        assertThat(postImage.getFileKey()).isEqualTo("old.jpg");
        assertThat(postImage.getImageOrder()).isEqualTo(2);
    }

}