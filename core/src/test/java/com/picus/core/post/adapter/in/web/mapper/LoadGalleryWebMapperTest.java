package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadGalleryResponse;
import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class LoadGalleryWebMapperTest {

    private LoadGalleryWebMapper webMapper = new LoadGalleryWebMapper();
    @Test
    @DisplayName("LoadGalleryResult -> LoadGalleryResponse 매핑")
    public void toResponse() throws Exception {
        // given
        LoadGalleryResult appResp = LoadGalleryResult.builder()
                .postNo("post-123")
                .imageUrls(List.of("img.com"))
                .title("title")
                .oneLineDescription("one")
                .build();
        // when
        LoadGalleryResponse webResp = webMapper.toResponse(appResp);

        // then
        assertThat(webResp.postNo()).isEqualTo("post-123");
        assertThat(webResp.imageUrls()).containsExactly("img.com");
        assertThat(webResp.title()).isEqualTo("title");
        assertThat(webResp.oneLineDescription()).isEqualTo("one");
    }

}