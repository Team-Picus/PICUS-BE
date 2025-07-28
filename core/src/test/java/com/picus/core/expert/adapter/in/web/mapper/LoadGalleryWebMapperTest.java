package com.picus.core.expert.adapter.in.web.mapper;

import com.amazonaws.services.dynamodbv2.xspec.L;
import com.picus.core.expert.adapter.in.web.data.response.LoadGalleryWebResp;
import com.picus.core.post.application.port.in.response.LoadGalleryAppResp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class LoadGalleryWebMapperTest {

    private LoadGalleryWebMapper webMapper = new LoadGalleryWebMapper();
    @Test
    @DisplayName("LoadGalleryAppResp -> LoadGalleryWebResp 매핑")
    public void toWebResp() throws Exception {
        // given
        LoadGalleryAppResp appResp = LoadGalleryAppResp.builder()
                .postNo("post-123")
                .thumbnailUrl("img.com")
                .title("title")
                .oneLineDescription("one")
                .build();
        // when
        LoadGalleryWebResp webResp = webMapper.toWebResp(appResp);

        // then
        assertThat(webResp.postNo()).isEqualTo("post-123");
        assertThat(webResp.thumbnailUrl()).isEqualTo("img.com");
        assertThat(webResp.title()).isEqualTo("title");
        assertThat(webResp.oneLineDescription()).isEqualTo("one");
    }

}