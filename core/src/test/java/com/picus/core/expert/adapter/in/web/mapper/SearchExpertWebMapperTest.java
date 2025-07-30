package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertResponse;
import com.picus.core.expert.application.port.in.result.SearchExpertResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class SearchExpertWebMapperTest {

    private final SearchExpertWebMapper mapper = new SearchExpertWebMapper();

    @Test
    @DisplayName("SearchExpertAppResponse를 SearchExpertWebResponse로 변환한다")
    void toWebResponse_shouldMapCorrectly() {
        // given
        SearchExpertResult appResponse = SearchExpertResult.builder()
                .expertNo("ex-001")
                .nickname("전문가A")
                .profileImageUrl("https://cdn.com/image.jpg")
                .build();

        // when
        SearchExpertResponse webResponse = mapper.toWebResponse(appResponse);

        // then
        assertThat(webResponse.expertNo()).isEqualTo("ex-001");
        assertThat(webResponse.nickname()).isEqualTo("전문가A");
        assertThat(webResponse.profileImageUrl()).isEqualTo("https://cdn.com/image.jpg");
    }
}