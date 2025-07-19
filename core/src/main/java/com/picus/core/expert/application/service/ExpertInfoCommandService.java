package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.ExpertInfoCommand;
import com.picus.core.expert.application.port.in.command.ExpertBasicInfoCommandRequest;
import com.picus.core.expert.application.port.in.command.ExpertDetailInfoCommandRequest;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserCommandPort;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class ExpertInfoCommandService implements ExpertInfoCommand {

    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final LoadExpertPort loadExpertPort;
    private final UpdateExpertPort updateExpertPort;

    @Override
    public void updateExpertBasicInfo(ExpertBasicInfoCommandRequest basicInfoRequest) {

        if(!shouldUpdate(basicInfoRequest))
            return;

        String expertNo = getExpertNo(basicInfoRequest);

        // Expert쪽 수정될 필요가 있는지 확인
        if (shouldUpdateExpertBasicInfo(basicInfoRequest)) {
            // Expert 로드
            Expert expert = loadExpertPort.findById(expertNo)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));
            // Expert 수정
            expert.updateBasicInfo(
                    basicInfoRequest.backgroundImageFileKey(),
                    basicInfoRequest.link(),
                    basicInfoRequest.intro());

            updateExpertPort.updateExpert(expert);
        }

        // User쪽 정보가 수정될 필요가 있는지 확인
        if (shouldUpdateUserInfo(basicInfoRequest)) {
            // User 정보 로드
            UserWithProfileImageDto userWithProfileImageDto = userQueryPort.findUserInfoByExpertNo(expertNo)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));

            // User 수정
            UserWithProfileImageDto updatedDto = UserWithProfileImageDto.builder()
                    .nickname(basicInfoRequest.nickname() != null ?
                            basicInfoRequest.nickname() : userWithProfileImageDto.nickname())
                    .profileImageFileKey(basicInfoRequest.profileImageFileKey() != null
                            ? basicInfoRequest.profileImageFileKey() : userWithProfileImageDto.profileImageFileKey())
                    .expertNo(expertNo)
                    .build();
            userCommandPort.updateNicknameAndImageByExpertNo(updatedDto);
        }
    }

    private String getExpertNo(ExpertBasicInfoCommandRequest basicInfoRequest) {
        // ExpertNo를 알기 위해 현재 User 로드
        User currentUser = userQueryPort.findById(basicInfoRequest.currentUserNo());
        return currentUser.getExpertNo();
    }

    private boolean shouldUpdate(ExpertBasicInfoCommandRequest basicInfoRequest) {
        return shouldUpdateExpertBasicInfo(basicInfoRequest) || shouldUpdateUserInfo(basicInfoRequest);
    }

    private boolean shouldUpdateExpertBasicInfo(ExpertBasicInfoCommandRequest basicInfoRequest) {
        return basicInfoRequest.backgroundImageFileKey() != null ||
                basicInfoRequest.link() != null ||
                basicInfoRequest.intro() != null;
    }

    private boolean shouldUpdateUserInfo(ExpertBasicInfoCommandRequest basicInfoRequest) {
        return basicInfoRequest.profileImageFileKey() != null || basicInfoRequest.nickname() != null;
    }

    @Override
    public void updateExpertDetailInfo(ExpertDetailInfoCommandRequest detailInfoRequest) {

    }
}
