package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.SuggestPostResponse;
import com.picus.core.post.application.port.in.result.SuggestPostResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SuggestPostWebMapperTest {

    private SuggestPostWebMapper webMapper = new SuggestPostWebMapper();

    @Test
    @DisplayName("SuggestPostResult -> SuggestPostResponse 매핑")
    public void toResponse() throws Exception {
        // given
        SuggestPostResult result = SuggestPostResult.builder()
                .postNo("post-123")
                .title("제목")
                .build();

        // when
        SuggestPostResponse response = webMapper.toResponse(result);

        // then
        assertThat(response.postId()).isEqualTo("post-123");
        assertThat(response.title()).isEqualTo("제목");
    }

}