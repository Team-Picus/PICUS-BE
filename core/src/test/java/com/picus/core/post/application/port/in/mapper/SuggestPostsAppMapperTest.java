package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.response.SuggestPostsAppResp;
import com.picus.core.post.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SuggestPostsAppMapperTest {

    private SuggestPostsAppMapper appMapper = new SuggestPostsAppMapper();

    @Test
    @DisplayName("Post -> SuggestPostsAppResp 매핑")
    public void toAppResp() throws Exception {
        // given
        Post post = Post.builder()
                .postNo("post-123")
                .title("title")
                .build();

        // when
        SuggestPostsAppResp appResp = appMapper.toAppResp(post);

        // then
        assertThat(appResp.postNo()).isEqualTo("post-123");
        assertThat(appResp.title()).isEqualTo("title");
    }

}