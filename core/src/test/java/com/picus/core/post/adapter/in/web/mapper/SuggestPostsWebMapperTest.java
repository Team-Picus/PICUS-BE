package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.SuggestPostsResponse;
import com.picus.core.post.application.port.in.result.SuggestPostsResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SuggestPostsWebMapperTest {

    private SuggestPostsWebMapper webMapper = new SuggestPostsWebMapper();

    @Test
    @DisplayName("SuggestPostsResult -> SuggestPostsResponse 매핑")
    public void toResponse() throws Exception {
        // given
        SuggestPostsResult result = SuggestPostsResult.builder()
                .postNo("post-123")
                .title("제목")
                .build();

        // when
        SuggestPostsResponse response = webMapper.toResponse(result);

        // then
        assertThat(response.postId()).isEqualTo("post-123");
        assertThat(response.title()).isEqualTo("제목");
    }

}