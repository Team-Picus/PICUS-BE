package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.GetExpertInfoQuery;
import com.picus.core.expert.application.port.in.response.GetExpertBasicInfoAppResponse;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.application.port.out.response.UserWithProfileImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetExpertInfoService implements GetExpertInfoQuery {

    private final LoadExpertPort loadExpertPort;
    private final UserQueryPort userQueryPort;

    @Override
    public Expert getExpertDetailInfo(String expertNo) {
        // expertNo로 Expert 조회
        return loadExpertPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    @Override
    public GetExpertBasicInfoAppResponse getExpertBasicInfo(String expertNo) {
        // expertNo로 Expert 조회
        Expert expert = loadExpertPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // expertNo로 User 정보 조회
        UserWithProfileImageDto userInfo = userQueryPort.findUserInfoByExpertNo(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // TODO: 배경 이미지, 프로필 이미지 key->url 변환

        return GetExpertBasicInfoAppResponse.builder()
                .expertNo(expert.getExpertNo())
                .activityDuration(expert.getActivityDuration())
                .activityCount(expert.getActivityCount())
                .lastActivityAt(expert.getLastActivityAt())
                .intro(expert.getIntro())
                .backgroundImageUrl("")
                .nickname(userInfo.nickname())
                .profileImageUrl("")
                .build();
    }
}
