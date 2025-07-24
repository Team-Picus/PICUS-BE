package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.ExpertInfoCommand;
import com.picus.core.expert.application.port.in.request.*;
import com.picus.core.expert.application.port.in.mapper.ProjectCommandAppMapper;
import com.picus.core.expert.application.port.in.mapper.SkillCommandAppMapper;
import com.picus.core.expert.application.port.in.mapper.StudioCommandAppMapper;
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

import java.util.ArrayList;
import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class ExpertInfoCommandService implements ExpertInfoCommand {

    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final LoadExpertPort loadExpertPort;
    private final UpdateExpertPort updateExpertPort;

    private final ProjectCommandAppMapper projectCommandAppMapper;
    private final SkillCommandAppMapper skillCommandAppMapper;
    private final StudioCommandAppMapper studioCommandAppMapper;

    @Override
    public void updateExpertBasicInfo(UpdateExpertBasicInfoAppReq basicInfoRequest) {

        if (!shouldUpdate(basicInfoRequest))
            return;

        String expertNo = getExpertNo(basicInfoRequest.currentUserNo());

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

    @Override
    public void updateExpertDetailInfo(UpdateExpertDetailInfoAppReq detailInfoRequest) {

        // 수정할 필요가 있는지 확인
        if (!shouldUpdateExpertDetailInfo(detailInfoRequest))
            return;

        // 전문가 인덱스 가져오기
        String expertNo = getExpertNo(detailInfoRequest.currentUserNo());

        // 전문가 불러오기
        Expert expert = loadExpertPort.findById(expertNo)
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
        updateExpertPort.updateExpertWithDetail(expert, deletedProjectNos, deletedSkillNos, deletedStudioNo);
    }

    /**
     * private 메서드
     */
    private String getExpertNo(String userNo) {
        // ExpertNo를 알기 위해 현재 User 로드
        User currentUser = userQueryPort.findById(userNo);
        return currentUser.getExpertNo();
    }

    private boolean shouldUpdate(UpdateExpertBasicInfoAppReq basicInfoRequest) {
        return shouldUpdateExpertBasicInfo(basicInfoRequest) || shouldUpdateUserInfo(basicInfoRequest);
    }

    private boolean shouldUpdateExpertBasicInfo(UpdateExpertBasicInfoAppReq basicInfoRequest) {
        return basicInfoRequest.backgroundImageFileKey() != null ||
                basicInfoRequest.link() != null ||
                basicInfoRequest.intro() != null;
    }

    private boolean shouldUpdateUserInfo(UpdateExpertBasicInfoAppReq basicInfoRequest) {
        return basicInfoRequest.profileImageFileKey() != null || basicInfoRequest.nickname() != null;
    }

    private boolean shouldUpdateExpertDetailInfo(UpdateExpertDetailInfoAppReq detailInfoRequest) {
        return detailInfoRequest.activityCareer() != null ||
                !detailInfoRequest.activityAreas().isEmpty() ||
                !detailInfoRequest.projects().isEmpty() ||
                !detailInfoRequest.skills().isEmpty() ||
                detailInfoRequest.studio() != null;
    }

    private void updateProject(UpdateExpertDetailInfoAppReq detailInfoRequest, Expert expert, List<String> deletedProjectNos) {
        List<UpdateProjectAppReq> updateProjectAppReqs = detailInfoRequest.projects();
        for (UpdateProjectAppReq command : updateProjectAppReqs) {
            switch (command.changeStatus()) {
                case ChangeStatus.NEW:
                    expert.addProject(projectCommandAppMapper.toDomain(command));
                    break;
                case ChangeStatus.UPDATE:
                    expert.updateProject(projectCommandAppMapper.toDomain(command));
                    break;
                case ChangeStatus.DELETE:
                    expert.deleteProject(command.projectNo());
                    deletedProjectNos.add(command.projectNo());
                    break;
            }
        }
    }

    private void updateSkill(UpdateExpertDetailInfoAppReq detailInfoRequest, Expert expert, List<String> deletedSkillNos) {
        List<UpdateSkillAppReq> updateSkillAppReqs = detailInfoRequest.skills();
        for (UpdateSkillAppReq command : updateSkillAppReqs) {
            switch (command.changeStatus()) {
                case ChangeStatus.NEW:
                    expert.addSkill(skillCommandAppMapper.toDomain(command));
                    break;
                case ChangeStatus.UPDATE:
                    expert.updateSkill(skillCommandAppMapper.toDomain(command));
                    break;
                case ChangeStatus.DELETE:
                    expert.deleteSkill(command.skillNo());
                    deletedSkillNos.add(command.skillNo());
                    break;
            }
        }
    }

    private String updateStudio(UpdateExpertDetailInfoAppReq request, Expert expert) {
        UpdateStudioAppReq command = request.studio();
        switch (command.changeStatus()) {
            case ChangeStatus.NEW -> expert.addStudio(studioCommandAppMapper.toDomain(command));
            case ChangeStatus.UPDATE -> expert.updateStudio(studioCommandAppMapper.toDomain(command));
            case ChangeStatus.DELETE -> {
                expert.deleteStudio();
                return command.studioNo(); // 삭제된 번호 리턴
            }
        }
        return null; // 삭제가 아닌 경우 null 리턴
    }
}
