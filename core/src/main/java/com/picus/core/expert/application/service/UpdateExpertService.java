package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.UpdateExpertUseCase;
import com.picus.core.expert.application.port.in.command.*;
import com.picus.core.expert.application.port.in.mapper.UpdateProjectCommandMapper;
import com.picus.core.expert.application.port.in.mapper.UpdateSkillCommandMapper;
import com.picus.core.expert.application.port.in.mapper.UpdateStudioCommandMapper;
import com.picus.core.expert.application.port.out.ExpertReadPort;
import com.picus.core.expert.application.port.out.ExpertUpdatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserUpdatePort;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UpdateExpertService implements UpdateExpertUseCase {

    private final UserReadPort userReadPort;
    private final UserUpdatePort userUpdatePort;
    private final ExpertReadPort expertReadPort;
    private final ExpertUpdatePort expertUpdatePort;

    private final UpdateProjectCommandMapper updateProjectCommandMapper;
    private final UpdateSkillCommandMapper updateSkillCommandMapper;
    private final UpdateStudioCommandMapper updateStudioCommandMapper;

    @Override
    public void updateExpertBasicInfo(UpdateExpertBasicInfoCommand basicInfoRequest) {

        if (!shouldUpdate(basicInfoRequest))
            return;

        String expertNo = getExpertNo(basicInfoRequest.currentUserNo());

        // Expert쪽 수정될 필요가 있는지 확인
        if (shouldUpdateExpertBasicInfo(basicInfoRequest)) {
            // Expert 로드
            Expert expert = expertReadPort.findById(expertNo)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));
            // Expert 수정
            expert.updateBasicInfo(
                    basicInfoRequest.backgroundImageFileKey(),
                    basicInfoRequest.link(),
                    basicInfoRequest.intro());

            expertUpdatePort.update(expert);
        }

        // User쪽 정보가 수정될 필요가 있는지 확인
        if (shouldUpdateUserInfo(basicInfoRequest)) {
            // User 정보 로드
            UserWithProfileImageDto userWithProfileImageDto = userReadPort.findUserInfoByExpertNo(expertNo)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));

            // User 수정
            UserWithProfileImageDto updatedDto = UserWithProfileImageDto.builder()
                    .nickname(basicInfoRequest.nickname() != null ?
                            basicInfoRequest.nickname() : userWithProfileImageDto.nickname())
                    .profileImageFileKey(basicInfoRequest.profileImageFileKey() != null
                            ? basicInfoRequest.profileImageFileKey() : userWithProfileImageDto.profileImageFileKey())
                    .expertNo(expertNo)
                    .build();
            userUpdatePort.updateNicknameAndImageByExpertNo(updatedDto);
        }
    }

    @Override
    public void updateExpertDetailInfo(UpdateExpertDetailInfoCommand detailInfoRequest) {

        // 수정할 필요가 있는지 확인
        if (!shouldUpdateExpertDetailInfo(detailInfoRequest))
            return;

        // 전문가 인덱스 가져오기
        String expertNo = getExpertNo(detailInfoRequest.currentUserNo());

        // 전문가 불러오기
        Expert expert = expertReadPort.findById(expertNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 전문가 정보 수정하기
        expert.updateDetailInfo(detailInfoRequest.activityCareer(), detailInfoRequest.activityAreas());

        // 프로젝트 정보 수정하기
        List<String> deletedProjectNos = new ArrayList<>();
        updateProject(detailInfoRequest, expert, deletedProjectNos);

        // Skill 정보 수정하기
        List<String> deletedSkillNos = new ArrayList<>();
        updateSkill(detailInfoRequest, expert, deletedSkillNos);

        // Studio 정보 수정하기
        String deletedStudioNo = updateStudio(detailInfoRequest, expert);

        // 수정된 정보 데이터베이스에 반영하기
        expertUpdatePort.update(expert, deletedProjectNos, deletedSkillNos, deletedStudioNo);
    }

    /**
     * private 메서드
     */
    private String getExpertNo(String userNo) {
        // ExpertNo를 알기 위해 현재 User 로드
        User currentUser = userReadPort.findById(userNo);
        return currentUser.getExpertNo();
    }

    private boolean shouldUpdate(UpdateExpertBasicInfoCommand basicInfoRequest) {
        return shouldUpdateExpertBasicInfo(basicInfoRequest) || shouldUpdateUserInfo(basicInfoRequest);
    }

    private boolean shouldUpdateExpertBasicInfo(UpdateExpertBasicInfoCommand basicInfoRequest) {
        return basicInfoRequest.backgroundImageFileKey() != null ||
                basicInfoRequest.link() != null ||
                basicInfoRequest.intro() != null;
    }

    private boolean shouldUpdateUserInfo(UpdateExpertBasicInfoCommand basicInfoRequest) {
        return basicInfoRequest.profileImageFileKey() != null || basicInfoRequest.nickname() != null;
    }

    private boolean shouldUpdateExpertDetailInfo(UpdateExpertDetailInfoCommand detailInfoRequest) {
        return detailInfoRequest.activityCareer() != null ||
                !detailInfoRequest.activityAreas().isEmpty() ||
                !detailInfoRequest.projects().isEmpty() ||
                !detailInfoRequest.skills().isEmpty() ||
                detailInfoRequest.studio() != null;
    }

    private void updateProject(UpdateExpertDetailInfoCommand detailInfoRequest, Expert expert, List<String> deletedProjectNos) {
        List<UpdateProjectCommand> updateProjectCommands = detailInfoRequest.projects();
        for (UpdateProjectCommand command : updateProjectCommands) {
            switch (command.changeStatus()) {
                case ChangeStatus.NEW:
                    expert.addProject(updateProjectCommandMapper.toDomain(command));
                    break;
                case ChangeStatus.UPDATE:
                    expert.updateProject(updateProjectCommandMapper.toDomain(command));
                    break;
                case ChangeStatus.DELETE:
                    expert.deleteProject(command.projectNo());
                    deletedProjectNos.add(command.projectNo());
                    break;
            }
        }
    }

    private void updateSkill(UpdateExpertDetailInfoCommand detailInfoRequest, Expert expert, List<String> deletedSkillNos) {
        List<UpdateSkillCommand> updateSkillCommands = detailInfoRequest.skills();
        for (UpdateSkillCommand command : updateSkillCommands) {
            switch (command.changeStatus()) {
                case ChangeStatus.NEW:
                    expert.addSkill(updateSkillCommandMapper.toDomain(command));
                    break;
                case ChangeStatus.UPDATE:
                    expert.updateSkill(updateSkillCommandMapper.toDomain(command));
                    break;
                case ChangeStatus.DELETE:
                    expert.deleteSkill(command.skillNo());
                    deletedSkillNos.add(command.skillNo());
                    break;
            }
        }
    }

    private String updateStudio(UpdateExpertDetailInfoCommand request, Expert expert) {
        UpdateStudioCommand command = request.studio();
        switch (command.changeStatus()) {
            case ChangeStatus.NEW -> expert.addStudio(updateStudioCommandMapper.toDomain(command));
            case ChangeStatus.UPDATE -> expert.updateStudio(updateStudioCommandMapper.toDomain(command));
            case ChangeStatus.DELETE -> {
                expert.deleteStudio();
                return command.studioNo(); // 삭제된 번호 리턴
            }
        }
        return null; // 삭제가 아닌 경우 null 리턴
    }
}
