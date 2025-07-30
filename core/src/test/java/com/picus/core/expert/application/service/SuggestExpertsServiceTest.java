package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.response.SuggestExpertResult;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class SuggestExpertsServiceTest {

    private final UserReadPort userReadPort = Mockito.mock(UserReadPort.class);
    private final SuggestExpertsService suggestExpertsService = new SuggestExpertsService(userReadPort);

    @Test
    @DisplayName("특정 키워드가 포함된 닉네임을 가진 전문가 n명 찾기 서비스 메서드의 리턴값 및 상호작용을 검증")
    public void suggestExperts_success() throws Exception {
        // given
        String testExpertNo = "test_expert_no";
        String testNickname = "test_nickname";
        String testFileKey = "test_file_key";
        List<UserWithProfileImageDto> testDtos = List.of(
                UserWithProfileImageDto.builder()
                        .expertNo(testExpertNo)
                        .nickname(testNickname)
                        .profileImageFileKey(testFileKey)
                        .build()
        );

        given(userReadPort.findTopNUserInfoByNicknameContainingOrderByNickname(any(String.class), any(Integer.class)))
                .willReturn(testDtos);

        // when
        List<SuggestExpertResult> results
                = suggestExpertsService.suggestExperts("any_keyword", 1);

        // then
        // TODO: profileImageUrl 검증
        assertThat(results).hasSize(1)
                .extracting(
                        SuggestExpertResult::expertNo,
                        SuggestExpertResult::nickname
                        )
                .containsExactlyInAnyOrder(tuple(testExpertNo, testNickname));

        then(userReadPort).should()
                .findTopNUserInfoByNicknameContainingOrderByNickname(any(String.class), any(Integer.class));
    }
}