package com.picus.core.post.application.port.in.mapper;

import com.picus.core.post.application.port.in.result.SuggestPostResult;
import com.picus.core.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SuggestPostCommandMapperTest {

    private SuggestPostCommandMapper appMapper = new SuggestPostCommandMapper();

    @Test
    @DisplayName("Post -> SuggestPostResult 매핑")
    public void toResult() throws Exception {
        // given
        Post post = Post.builder()
                .postNo("post-123")
                .title("title")
                .build();

        // when
        SuggestPostResult appResp = appMapper.toResult(post);

        // then
        assertThat(appResp.postNo()).isEqualTo("post-123");
        assertThat(appResp.title()).isEqualTo("title");
    }

}