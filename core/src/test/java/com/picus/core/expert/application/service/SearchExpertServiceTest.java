package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.ProfileImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

class SearchExpertServiceTest {

    private final LoadExpertPort loadExpertPort = Mockito.mock(LoadExpertPort.class);
    private final UserQueryPort userQueryPort = Mockito.mock(UserQueryPort.class);
    private final SearchExpertsService searchExpertService = new SearchExpertsService(loadExpertPort, userQueryPort);

    @Test
    @DisplayName("특정 키워드가 포함된 닉네임을 가진 전문가 찾기 서비스 메서드의 리턴값 및 상호작용 검증")
    public void searchExperts_success() throws Exception {
        // given
        List<SearchExpertAppResponse> sampleResponses = List.of(
                SearchExpertAppResponse.builder()
                        .expertNo("test_expert_no")
                        .nickname("test_nickname")
                        .build()
        );
        BDDMockito.given(loadExpertPort.findByNicknameContaining(any(String.class)))
                .willReturn(sampleResponses);
        BDDMockito.given(userQueryPort.findProfileImageByExpertNo(any(String.class)))
                .willReturn(any(ProfileImage.class));

        // when
        List<SearchExpertAppResponse> results = searchExpertService.searchExperts("any_keyword");

        // then
        assertThat(results).hasSize(1)
                .extracting("expertNo", "nickname")
                .containsExactlyInAnyOrder(tuple("test_expert_no", "test_nickname"));

        then(loadExpertPort).should()
                .findByNicknameContaining(any(String.class));
        then(userQueryPort).should()
                .findProfileImageByExpertNo(any(String.class));
    }

}