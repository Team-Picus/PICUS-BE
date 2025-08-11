package com.picus.core.post.adapter.in.web.mapper;

import com.picus.core.post.adapter.in.web.data.response.LoadGalleryResponse;
import com.picus.core.post.adapter.in.web.data.response.LoadGalleryResponse.PostImageResponse;
import com.picus.core.post.application.port.in.result.LoadGalleryResult;
import com.picus.core.post.application.port.in.result.LoadGalleryResult.PostImageResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;


class LoadGalleryWebMapperTest {

    private LoadGalleryWebMapper webMapper = new LoadGalleryWebMapper();

    @Test
    @DisplayName("LoadGalleryResult -> LoadGalleryResponse 매핑")
    public void toResponse() throws Exception {
        // given
        LoadGalleryResult appResp = LoadGalleryResult.builder()
                .postNo("post-123")
                .images(List.of(
                        PostImageResult.builder()
                                .imageNo("pi-123")
                                .fileKey("k-123")
                                .imageUrl("img.com")
                                .imageOrder(1)
                                .build()))
                .title("title")
                .oneLineDescription("one")
                .build();
        // when
        LoadGalleryResponse webResp = webMapper.toResponse(appResp);

        // then
        assertThat(webResp.postNo()).isEqualTo("post-123");
        assertThat(webResp.images()).hasSize(1)
                .extracting(
                        PostImageResponse::imageNo,
                        PostImageResponse::fileKey,
                        PostImageResponse::imageUrl,
                        PostImageResponse::imageOrder
                ).containsExactly(tuple("pi-123", "k-123", "img.com", 1));
        assertThat(webResp.title()).isEqualTo("title");
        assertThat(webResp.oneLineDescription()).isEqualTo("one");
    }

}