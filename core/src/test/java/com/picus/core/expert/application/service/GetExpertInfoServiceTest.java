package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.response.GetExpertBasicInfoAppResponse;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

class GetExpertInfoServiceTest {

    private final LoadExpertPort loadExpertPort = Mockito.mock(LoadExpertPort.class);
    private final UserQueryPort userQueryPort = Mockito.mock(UserQueryPort.class);

    private final GetExpertInfoService getExpertService = new GetExpertInfoService(loadExpertPort, userQueryPort);

    @Test
    @DisplayName("특정 전문가를 조회하는 서비스 메서드의 리턴값 및 상호작용을 검증한다.")
    public void getExpertBasicInfo_success() throws Exception {
        // given
        String expertNo = "expertNo1";
        Expert expert = givenExpertDomain(
                expertNo,
                "소개입니다",
                "back_key",
                "5년",
                List.of("서울 강북구"),
                10,
                LocalDateTime.of(2024, 5, 10, 10, 0)
        );
        UserWithProfileImageDto dto = givenUserWithProfileImageDto(
                "nickname",
                "profile_key",
                "expert_123"
        );
        stubPortResult(expert, dto, expertNo);

        // when
        GetExpertBasicInfoAppResponse result = getExpertService.getExpertBasicInfo(expertNo);

        // then - 리턴값 검증
        // TODO: 이미지 key -> url 변환 로직 추가 후 다시 검증
        assertThat(result.expertNo()).isEqualTo(expert.getExpertNo());
        assertThat(result.activityDuration()).isEqualTo(expert.getActivityDuration());
        assertThat(result.activityCount()).isEqualTo(expert.getActivityCount());
        assertThat(result.lastActivityAt()).isEqualTo(expert.getLastActivityAt());
        assertThat(result.intro()).isEqualTo(expert.getIntro());
        assertThat(result.nickname()).isEqualTo(dto.nickname());

        // then - 메서드 상호작용 검증
        then(loadExpertPort).should().findById(eq(expertNo));
        then(userQueryPort).should().findUserInfoByExpertNo(eq(expertNo));
    }

    private void stubPortResult(Expert expert, UserWithProfileImageDto userWithProfileImageDto, String expertNo) {
        // LoadExpertPort Stubbing
        given(loadExpertPort.findById(expertNo))
                .willReturn(Optional.of(expert));

        // UserQueryPort Stubbing
        given(userQueryPort.findUserInfoByExpertNo(expertNo))
                .willReturn(Optional.of(userWithProfileImageDto));
    }

    private Expert givenExpertDomain(
            String expertNo,
            String intro,
            String backgroundImageKey,
            String activityCareer,
            List<String> activityAreas,
            int activityCount,
            LocalDateTime lastActivityAt
    ) {
        return Expert.builder()
                .expertNo(expertNo)
                .intro(intro)
                .backgroundImageKey(backgroundImageKey)
                .activityCareer(activityCareer)
                .activityAreas(activityAreas)
                .activityCount(activityCount)
                .lastActivityAt(lastActivityAt)
                .build();
    }

    private UserWithProfileImageDto givenUserWithProfileImageDto(
            String nickname,
            String profileImageFileKey,
            String expertNo
    ) {
        return UserWithProfileImageDto.builder()
                .nickname(nickname)
                .profileImageFileKey(profileImageFileKey)
                .expertNo(expertNo)
                .build();
    }

}