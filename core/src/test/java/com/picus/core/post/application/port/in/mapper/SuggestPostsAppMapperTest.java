package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.response.SuggestPostsResult;
import com.picus.core.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SuggestPostsAppMapperTest {

    private SuggestPostsAppMapper appMapper = new SuggestPostsAppMapper();

    @Test
    @DisplayName("Post -> SuggestPostsResult 매핑")
    public void toAppResp() throws Exception {
        // given
        Post post = Post.builder()
                .postNo("post-123")
                .title("title")
                .build();

        // when
        SuggestPostsResult appResp = appMapper.toAppResp(post);

        // then
        assertThat(appResp.postNo()).isEqualTo("post-123");
        assertThat(appResp.title()).isEqualTo("title");
    }

}