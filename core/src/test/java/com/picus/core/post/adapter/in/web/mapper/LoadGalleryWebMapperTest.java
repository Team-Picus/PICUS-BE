package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadGalleryResponse;
import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class LoadGalleryWebMapperTest {

    private LoadGalleryWebMapper webMapper = new LoadGalleryWebMapper();
    @Test
    @DisplayName("LoadGalleryResult -> LoadGalleryResponse 매핑")
    public void toWebResp() throws Exception {
        // given
        LoadGalleryResult appResp = LoadGalleryResult.builder()
                .postNo("post-123")
                .thumbnailUrl("img.com")
                .title("title")
                .oneLineDescription("one")
                .build();
        // when
        LoadGalleryResponse webResp = webMapper.toWebResp(appResp);

        // then
        assertThat(webResp.postNo()).isEqualTo("post-123");
        assertThat(webResp.thumbnailUrl()).isEqualTo("img.com");
        assertThat(webResp.title()).isEqualTo("title");
        assertThat(webResp.oneLineDescription()).isEqualTo("one");
    }

}