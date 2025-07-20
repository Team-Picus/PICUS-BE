package com.picus.core.expert.adapter.in.web.mapper;

import com.picus.core.expert.adapter.in.web.data.request.ExpertBasicInfoCommandWebRequest;
import com.picus.core.expert.application.port.in.command.ExpertBasicInfoCommandRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UpdateExpertWebMapperTest {

    private final UpdateExpertWebMapper mapper = new UpdateExpertWebMapper();

    @Test
    @DisplayName("ExpertBasicInfoCommandWebRequest를 ExpertBasicInfoCommandRequest로 변환한다.")
    void toBasicInfoAppRequest_should_map_all_fields_correctly() {
        // given
        String currentUserNo = "USER123";
        ExpertBasicInfoCommandWebRequest webRequest = new ExpertBasicInfoCommandWebRequest(
                "profile-img-key",
                "background-img-key",
                "test-nickname",
                "https://my.link",
                "소개 글입니다"
        );

        // when
        ExpertBasicInfoCommandRequest result = mapper.toBasicInfoAppRequest(webRequest, currentUserNo);

        // then
        assertThat(result.currentUserNo()).isEqualTo("USER123");
        assertThat(result.profileImageFileKey()).isEqualTo("profile-img-key");
        assertThat(result.backgroundImageFileKey()).isEqualTo("background-img-key");
        assertThat(result.nickname()).isEqualTo("test-nickname");
        assertThat(result.link()).isEqualTo("https://my.link");
        assertThat(result.intro()).isEqualTo("소개 글입니다");
    }
}