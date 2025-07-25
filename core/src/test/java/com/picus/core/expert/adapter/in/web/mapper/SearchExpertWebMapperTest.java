package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SearchExpertWebResponse;
import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class SearchExpertWebMapperTest {

    private final SearchExpertWebMapper mapper = new SearchExpertWebMapper();

    @Test
    @DisplayName("SearchExpertAppResponse를 SearchExpertWebResponse로 변환한다")
    void toWebResponse_shouldMapCorrectly() {
        // given
        SearchExpertAppResponse appResponse = SearchExpertAppResponse.builder()
                .expertNo("ex-001")
                .nickname("전문가A")
                .profileImageUrl("https://cdn.com/image.jpg")
                .build();

        // when
        SearchExpertWebResponse webResponse = mapper.toWebResponse(appResponse);

        // then
        assertThat(webResponse.expertNo()).isEqualTo("ex-001");
        assertThat(webResponse.nickname()).isEqualTo("전문가A");
        assertThat(webResponse.profileImageUrl()).isEqualTo("https://cdn.com/image.jpg");
    }
}