package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.LoadExpertUseCase;
import com.picus.core.expert.application.port.in.response.ExpertBasicInfoResult;
import com.picus.core.expert.application.port.out.ExpertReadPort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadExpertService implements LoadExpertUseCase {

    private final ExpertReadPort expertReadPort;
    private final UserQueryPort userQueryPort;

    @Override
    public Expert getExpertDetailInfo(String expertNo) {
        // expertNo로 Expert 조회
        return expertReadPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    @Override
    public ExpertBasicInfoResult getExpertBasicInfo(String expertNo) {
        // expertNo로 Expert 조회
        Expert expert = expertReadPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // expertNo로 User 정보 조회
        UserWithProfileImageDto userInfo = userQueryPort.findUserInfoByExpertNo(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // TODO: 배경 이미지, 프로필 이미지 key->url 변환

        return ExpertBasicInfoResult.builder()
                .expertNo(expert.getExpertNo())
                .activityDuration(expert.getActivityDuration())
                .activityCount(expert.getActivityCount())
                .lastActivityAt(expert.getLastActivityAt())
                .intro(expert.getIntro())
                .backgroundImageUrl("")
                .nickname(userInfo.nickname())
                .profileImageUrl("")
                .links(expert.getPortfolioLinks())
                .build();
    }
}
