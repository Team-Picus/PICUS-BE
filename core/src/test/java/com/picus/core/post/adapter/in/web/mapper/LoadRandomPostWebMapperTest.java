package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadRandomPostResponse;
import com.picus.core.post.application.port.in.result.LoadRandomPostResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LoadRandomPostWebMapperTest {

    private LoadRandomPostWebMapper webMapper = new LoadRandomPostWebMapper();
    @Test
    @DisplayName("LoadRandomPostResult -> LoadRandomPostResponse 매핑")
    public void toResponse() throws Exception {
        // given
        LoadRandomPostResult result = LoadRandomPostResult.builder()
                .postNo("post-123")
                .nickname("nick1")
                .thumbnailUrl("img.com")
                .build();

        // when
        LoadRandomPostResponse response = webMapper.toResponse(result);

        // then
        assertThat(response.postNo()).isEqualTo("post-123");
        assertThat(response.nickname()).isEqualTo("nick1");
        assertThat(response.thumbnailUrl()).isEqualTo("img.com");
    }

}