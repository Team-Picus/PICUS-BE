package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import com.picus.core.expert.application.port.in.response.SuggestExpertAppResponse;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.ProfileImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class SuggestExpertsServiceTest {

    private final LoadExpertPort loadExpertPort = Mockito.mock(LoadExpertPort.class);
    private final UserQueryPort userQueryPort = Mockito.mock(UserQueryPort.class);
    private final SuggestExpertsService suggestExpertsService = new SuggestExpertsService(loadExpertPort, userQueryPort);

    @Test
    @DisplayName("특정 키워드가 포함된 닉네임을 가진 전문가 n명 찾기 서비스 메서드의 리턴값 및 상호작용을 검증")
    public void suggestExperts_success() throws Exception {
        // given
        List<SuggestExpertAppResponse> sampleResponses = List.of(
                SuggestExpertAppResponse.builder()
                        .expertNo("test_expert_no")
                        .nickname("test_nickname")
                        .build()
        );
        ProfileImage mockImage = Mockito.mock(ProfileImage.class);
        given(loadExpertPort.findByNicknameContainingLimited(any(String.class), any(Integer.class)))
                .willReturn(sampleResponses);
        given(userQueryPort.findProfileImageByExpertNo(any(String.class)))
                .willReturn(mockImage);


        // when
        List<SuggestExpertAppResponse> results
                = suggestExpertsService.suggestExperts("any_keyword", 1);

        // then
        assertThat(results).hasSize(1)
                .extracting("expertNo", "nickname")
                .containsExactlyInAnyOrder(tuple("test_expert_no", "test_nickname"));

        then(loadExpertPort).should()
                .findByNicknameContainingLimited(any(String.class), any(Integer.class));
        then(userQueryPort).should()
                .findProfileImageByExpertNo(any(String.class));
    }
}