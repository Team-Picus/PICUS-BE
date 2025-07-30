package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.response.SuggestExpertResponse;
import com.picus.core.expert.application.port.in.result.SuggestExpertResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class SuggestExpertWebMapperTest {

    private final SuggestExpertWebMapper mapper = new SuggestExpertWebMapper();

    @Test
    @DisplayName("SuggestExpertAppResponse를 SuggestExpertWebResponse로 변환한다")
    void toWebResponse_shouldMapCorrectly() {
        // given
        SuggestExpertResult appResponse = SuggestExpertResult.builder()
                .expertNo("ex123")
                .nickname("닉네임")
                .profileImageUrl("https://cdn.com/img.jpg")
                .build();

        // when
        SuggestExpertResponse webResponse = mapper.toWebResponse(appResponse);

        // then
        assertThat(webResponse.expertNo()).isEqualTo("ex123");
        assertThat(webResponse.nickname()).isEqualTo("닉네임");
        assertThat(webResponse.profileImageUrl()).isEqualTo("https://cdn.com/img.jpg");
    }
}