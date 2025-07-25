package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.response.SearchExpertAppResponse;
import com.picus.core.user.application.port.out.UserQueryPort;
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

class SearchExpertsServiceTest {

    private final UserQueryPort userQueryPort = Mockito.mock(UserQueryPort.class);
    private final SearchExpertsService searchExpertService = new SearchExpertsService(userQueryPort);

    @Test
    @DisplayName("특정 키워드가 포함된 닉네임을 가진 전문가 찾기 서비스 메서드의 리턴값 및 상호작용 검증")
    public void searchExperts_success() throws Exception {
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

        given(userQueryPort.findUserInfoByNicknameContaining(any(String.class)))
                .willReturn(testDtos);

        // when
        List<SearchExpertAppResponse> results = searchExpertService.searchExperts("any_keyword");

        // then
        // TODO: profileImageUrl 검증
        assertThat(results).hasSize(1)
                .extracting(
                        SearchExpertAppResponse::expertNo,
                        SearchExpertAppResponse::nickname)
                .containsExactlyInAnyOrder(tuple(testExpertNo, testNickname));

        then(userQueryPort).should()
                .findUserInfoByNicknameContaining(any(String.class));
    }

}