package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.request.*;
import com.picus.core.expert.application.port.in.mapper.UpdateProjectAppMapper;
import com.picus.core.expert.application.port.in.mapper.UpdateSkillAppMapper;
import com.picus.core.expert.application.port.in.mapper.UpdateStudioAppMapper;
import com.picus.core.expert.application.port.out.ExpertQueryPort;
import com.picus.core.expert.application.port.out.ExpertCommandPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.SkillType;
import com.picus.core.user.application.port.out.UserCommandPort;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class ExpertInfoCommandServiceTest {


    UserQueryPort userQueryPort = mock(UserQueryPort.class);
    UserCommandPort userCommandPort = mock(UserCommandPort.class);
    ExpertQueryPort expertQueryPort = mock(ExpertQueryPort.class);
    ExpertCommandPort expertCommandPort = mock(ExpertCommandPort.class);


    UpdateProjectAppMapper updateProjectAppMapper = mock(UpdateProjectAppMapper.class);
    UpdateSkillAppMapper updateSkillAppMapper = mock(UpdateSkillAppMapper.class);
    UpdateStudioAppMapper updateStudioAppMapper = mock(UpdateStudioAppMapper.class);

    ExpertInfoCommandService expertInfoCommandService =
            new ExpertInfoCommandService(userQueryPort, userCommandPort, expertQueryPort, expertCommandPort,
                    updateProjectAppMapper, updateSkillAppMapper, updateStudioAppMapper);

    @Test
    @DisplayName("Expert와 User 기본 정보가 모두 수정되는 경우 update 호출이 수행된다.")
    void updateExpertBasicInfo_success() {
        // given
        String userNo = "USER001";
        String expertNo = "EXP001";

        UpdateExpertBasicInfoAppReq request = UpdateExpertBasicInfoAppReq.builder()
                .currentUserNo(userNo)
                .backgroundImageFileKey("new-background")
                .link(List.of("https://new.link"))
                .intro("New intro")
                .nickname("NewNickname")
                .profileImageFileKey("new-profile-img")
                .build();

        Expert expert = mock(Expert.class);

        UserWithProfileImageDto userWithProfile = UserWithProfileImageDto.builder()
                .expertNo(expertNo)
                .nickname("OldNickname")
                .profileImageFileKey("old-profile-img")
                .build();

        User user = mock(User.class);
        when(user.getExpertNo()).thenReturn(expertNo);

        given(userQueryPort.findById(userNo)).willReturn(user);
        given(expertQueryPort.findById(expertNo)).willReturn(Optional.of(expert));
        given(userQueryPort.findUserInfoByExpertNo(expertNo)).willReturn(Optional.of(userWithProfile));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userQueryPort).should().findById(userNo);
        then(expertQueryPort).should().findById(expertNo);
        then(expert).should().updateBasicInfo("new-background", List.of("https://new.link"), "New intro");
        then(expertCommandPort).should().updateExpert(expert);
        then(userQueryPort).should().findUserInfoByExpertNo(expertNo);
        then(userCommandPort).should().updateNicknameAndImageByExpertNo(argThat(updatedDto ->
                updatedDto.nickname().equals("NewNickname") &&
                        updatedDto.profileImageFileKey().equals("new-profile-img") &&
                        updatedDto.expertNo().equals(expertNo)
        ));
    }

    @Test
    @DisplayName("Expert 정보만 수정되는 경우, Expert만 update 된다.")
    void updateExpertBasicInfo_onlyExpertChanged() {
        // given
        String userNo = "USER001";
        String expertNo = "EXP001";

        UpdateExpertBasicInfoAppReq request = UpdateExpertBasicInfoAppReq.builder()
                .currentUserNo(userNo)
                .backgroundImageFileKey("bg-key") // expert 관련 필드만 있음
                .link(List.of("https://new.link"))
                .intro("new intro")
                .build();

        Expert expert = mock(Expert.class);
        User user = mock(User.class);

        given(userQueryPort.findById(userNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(expertNo);
        given(expertQueryPort.findById(expertNo)).willReturn(Optional.of(expert));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userQueryPort).should().findById(userNo);
        then(expertQueryPort).should().findById(expertNo);
        then(expert).should().updateBasicInfo("bg-key", List.of("https://new.link"), "new intro");
        then(expertCommandPort).should().updateExpert(expert);
        then(userCommandPort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("User 정보만 수정되는 경우, User만 update 된다.")
    void updateExpertBasicInfo_onlyUserChanged() {
        // given
        String userNo = "USER002";
        String expertNo = "EXP002";

        UpdateExpertBasicInfoAppReq request = UpdateExpertBasicInfoAppReq.builder()
                .currentUserNo(userNo)
                .nickname("UpdatedNickname")
                .profileImageFileKey("updated-profile-img")
                .build();

        User user = mock(User.class);
        when(user.getExpertNo()).thenReturn(expertNo);

        UserWithProfileImageDto userWithProfile = UserWithProfileImageDto.builder()
                .expertNo(expertNo)
                .nickname("OldNickname")
                .profileImageFileKey("old-img")
                .build();

        given(userQueryPort.findById(userNo)).willReturn(user);
        given(userQueryPort.findUserInfoByExpertNo(expertNo)).willReturn(Optional.of(userWithProfile));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userQueryPort).should().findById(userNo);
        then(userQueryPort).should().findUserInfoByExpertNo(expertNo);
        then(userCommandPort).should().updateNicknameAndImageByExpertNo(argThat(dto ->
                dto.nickname().equals("UpdatedNickname") &&
                        dto.profileImageFileKey().equals("updated-profile-img") &&
                        dto.expertNo().equals(expertNo)
        ));
        then(expertQueryPort).shouldHaveNoInteractions();
        then(expertCommandPort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("모든 값이 null일 경우 아무 업데이트도 수행되지 않는다.")
    void updateExpertBasicInfo_nothingChanged() {
        // given
        String userNo = "USER003";

        UpdateExpertBasicInfoAppReq request = UpdateExpertBasicInfoAppReq.builder()
                .currentUserNo(userNo)
                .build(); // 모든 필드가 null

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userQueryPort).shouldHaveNoInteractions();
        then(expertQueryPort).shouldHaveNoInteractions();
        then(expertCommandPort).shouldHaveNoInteractions();
        then(userCommandPort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Expert 상세 정보가 모두 수정되는 경우 updateExpertWithDetail이 호출된다.")
    void updateExpertDetailInfo_success() {
        // given
        String userNo = "USR001";
        String expertNo = "EXP001";

        // --- 요청 DTO 구성
        UpdateProjectAppReq projectNew = createProjectCommand(null, "추가된 프로젝트",
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 11, 0), ChangeStatus.NEW);
        UpdateProjectAppReq projectUpdate = createProjectCommand("PRJ002", "수정된 프로젝트",
                LocalDateTime.of(2023, 12, 1, 10, 0),
                LocalDateTime.of(2023, 12, 1, 11, 0), ChangeStatus.UPDATE);
        UpdateProjectAppReq projectDelete = createProjectCommand("PRJ003", "삭제된 프로젝트",
                LocalDateTime.of(2022, 11, 1, 10, 0),
                LocalDateTime.of(2022, 11, 1, 11, 0), ChangeStatus.DELETE);

        UpdateSkillAppReq skillNew = createSkillCommand(null, SkillType.LIGHT, "조명 가능", ChangeStatus.NEW);
        UpdateSkillAppReq skillUpdate = createSkillCommand("SKILL002", SkillType.CAMERA, "카메라 전문가", ChangeStatus.UPDATE);
        UpdateSkillAppReq skillDelete = createSkillCommand("SKILL003", SkillType.EDIT, "편집 불필요", ChangeStatus.DELETE);

        UpdateStudioAppReq studioNew = createStudioCommand(null, "스튜디오 신규", 2, "09:00~18:00", "서울시 강남구", ChangeStatus.NEW);

        UpdateExpertDetailInfoAppReq request = UpdateExpertDetailInfoAppReq.builder()
                .currentUserNo(userNo)
                .activityCareer("5년 경력")
                .activityAreas(List.of("서울", "부산"))
                .projects(List.of(projectNew, projectUpdate, projectDelete))
                .skills(List.of(skillNew, skillUpdate, skillDelete))
                .studio(studioNew)
                .build();

        // --- Mock 정의
        User user = mock(User.class);
        given(userQueryPort.findById(userNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(expertNo);

        Expert expert = mock(Expert.class);
        given(expertQueryPort.findById(expertNo)).willReturn(Optional.of(expert));

        Project pjNew = mock(Project.class);
        Project pjUpdate = mock(Project.class);
        given(updateProjectAppMapper.toDomain(projectNew)).willReturn(pjNew);
        given(updateProjectAppMapper.toDomain(projectUpdate)).willReturn(pjUpdate);

        Skill skNew = mock(Skill.class);
        Skill skUpdate = mock(Skill.class);
        given(updateSkillAppMapper.toDomain(skillNew)).willReturn(skNew);
        given(updateSkillAppMapper.toDomain(skillUpdate)).willReturn(skUpdate);

        Studio studio = mock(Studio.class);
        given(updateStudioAppMapper.toDomain(studioNew)).willReturn(studio);

        // when
        expertInfoCommandService.updateExpertDetailInfo(request);

        // then
        InOrder inOrder = inOrder(
                userQueryPort, user,
                expertQueryPort, expert,
                updateProjectAppMapper, expert,
                updateSkillAppMapper, expert,
                updateStudioAppMapper, expert,
                expertCommandPort
        );

        then(userQueryPort).should(inOrder).findById(userNo);
        then(user).should(inOrder).getExpertNo();
        then(expertQueryPort).should(inOrder).findById(expertNo);

        then(updateProjectAppMapper).should(inOrder).toDomain(projectNew);
        then(expert).should(inOrder).addProject(pjNew);
        then(updateProjectAppMapper).should(inOrder).toDomain(projectUpdate);
        then(expert).should(inOrder).updateProject(pjUpdate);
        then(expert).should(inOrder).deleteProject("PRJ003");

        then(updateSkillAppMapper).should(inOrder).toDomain(skillNew);
        then(expert).should(inOrder).addSkill(skNew);
        then(updateSkillAppMapper).should(inOrder).toDomain(skillUpdate);
        then(expert).should(inOrder).updateSkill(skUpdate);
        then(expert).should(inOrder).deleteSkill("SKILL003");

        then(updateStudioAppMapper).should(inOrder).toDomain(studioNew);
        then(expert).should(inOrder).addStudio(studio);

        then(expertCommandPort).should(inOrder).updateExpertWithDetail(
                eq(expert),
                eq(List.of("PRJ003")),
                eq(List.of("SKILL003")),
                eq(null)
        );

        then(expertCommandPort).shouldHaveNoMoreInteractions();
    }

    private UpdateProjectAppReq createProjectCommand(String projectNo, String projectName, LocalDateTime startDate, LocalDateTime endDate, ChangeStatus changeStatus) {
        return UpdateProjectAppReq.builder()
                .projectNo(projectNo)
                .projectName(projectName)
                .startDate(startDate)
                .endDate(endDate)
                .changeStatus(changeStatus)
                .build();
    }

    private UpdateSkillAppReq createSkillCommand(String skillNo, SkillType skillType, String content, ChangeStatus changeStatus) {
        return UpdateSkillAppReq.builder()
                .skillNo(skillNo)
                .skillType(skillType)
                .content(content)
                .changeStatus(changeStatus)
                .build();
    }

    private UpdateStudioAppReq createStudioCommand(String studioNo, String studioName, int employeesCount, String businessHours, String address, ChangeStatus changeStatus) {
        return UpdateStudioAppReq.builder()
                .studioNo(studioNo)
                .studioName(studioName)
                .employeesCount(employeesCount)
                .businessHours(businessHours)
                .address(address)
                .changeStatus(changeStatus)
                .build();
    }

    @Test
    @DisplayName("모든 값이 null일 경우 updateExpertDetailInfo는 아무 작업도 하지 않는다.")
    void updateExpertDetailInfo_nothingChanged() {
        // given
        String userNo = "USER003";

        UpdateExpertDetailInfoAppReq request = UpdateExpertDetailInfoAppReq.builder()
                .currentUserNo(userNo)
                .build(); // 모든 필드가 null

        // when
        expertInfoCommandService.updateExpertDetailInfo(request);

        // then
        then(userQueryPort).shouldHaveNoInteractions();
        then(expertQueryPort).shouldHaveNoInteractions();
        then(expertCommandPort).shouldHaveNoInteractions();
        then(userCommandPort).shouldHaveNoInteractions();
    }
}