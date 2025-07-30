package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import com.picus.core.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoadGalleryCommandMapperTest {

    private LoadGalleryCommandMapper appMapper = new LoadGalleryCommandMapper();
    @Test
    @DisplayName("Post, thumbnailUrl -> LoadGalleryResult 매핑")
    public void toAppResp() throws Exception {
        // given
        Post post = Post.builder()
                .postNo("post-123")
                .title("제목")
                .oneLineDescription("한줄소개")
                .build();
        String thumbnailUrl = "img.com";
        
        // when
        LoadGalleryResult result = appMapper.toAppResp(post, thumbnailUrl);

        // then
        assertThat(result.postNo()).isEqualTo("post-123");
        assertThat(result.thumbnailUrl()).isEqualTo("img.com");
        assertThat(result.title()).isEqualTo("제목");
        assertThat(result.oneLineDescription()).isEqualTo("한줄소개");
    }

}