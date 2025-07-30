package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.command.*;
import com.picus.core.expert.application.port.in.mapper.ProjectCommandAppMapper;
import com.picus.core.expert.application.port.in.mapper.SkillCommandAppMapper;
import com.picus.core.expert.application.port.in.mapper.StudioCommandAppMapper;
import com.picus.core.expert.application.port.out.LoadExpertPort;
import com.picus.core.expert.application.port.out.UpdateExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.SkillType;
import com.picus.core.user.application.port.out.UserUpdatePort;
import com.picus.core.user.application.port.out.UserReadPort;
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


    UserReadPort userReadPort = mock(UserReadPort.class);
    UserUpdatePort userUpdatePort = mock(UserUpdatePort.class);
    LoadExpertPort loadExpertPort = mock(LoadExpertPort.class);
    UpdateExpertPort updateExpertPort = mock(UpdateExpertPort.class);


    ProjectCommandAppMapper projectCommandAppMapper = mock(ProjectCommandAppMapper.class);
    SkillCommandAppMapper skillCommandAppMapper = mock(SkillCommandAppMapper.class);
    StudioCommandAppMapper studioCommandAppMapper = mock(StudioCommandAppMapper.class);

    ExpertInfoCommandService expertInfoCommandService =
            new ExpertInfoCommandService(userReadPort, userUpdatePort, loadExpertPort, updateExpertPort,
                    projectCommandAppMapper, skillCommandAppMapper, studioCommandAppMapper);

    @Test
    @DisplayName("Expert와 User 기본 정보가 모두 수정되는 경우 update 호출이 수행된다.")
    void updateExpertBasicInfo_success() {
        // given
        String userNo = "USER001";
        String expertNo = "EXP001";

        UpdateExpertBasicInfoAppRequest request = UpdateExpertBasicInfoAppRequest.builder()
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

        given(userReadPort.findById(userNo)).willReturn(user);
        given(loadExpertPort.findById(expertNo)).willReturn(Optional.of(expert));
        given(userReadPort.findUserInfoByExpertNo(expertNo)).willReturn(Optional.of(userWithProfile));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userReadPort).should().findById(userNo);
        then(loadExpertPort).should().findById(expertNo);
        then(expert).should().updateBasicInfo("new-background", List.of("https://new.link"), "New intro");
        then(updateExpertPort).should().updateExpert(expert);
        then(userReadPort).should().findUserInfoByExpertNo(expertNo);
        then(userUpdatePort).should().updateNicknameAndImageByExpertNo(argThat(updatedDto ->
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

        UpdateExpertBasicInfoAppRequest request = UpdateExpertBasicInfoAppRequest.builder()
                .currentUserNo(userNo)
                .backgroundImageFileKey("bg-key") // expert 관련 필드만 있음
                .link(List.of("https://new.link"))
                .intro("new intro")
                .build();

        Expert expert = mock(Expert.class);
        User user = mock(User.class);

        given(userReadPort.findById(userNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(expertNo);
        given(loadExpertPort.findById(expertNo)).willReturn(Optional.of(expert));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userReadPort).should().findById(userNo);
        then(loadExpertPort).should().findById(expertNo);
        then(expert).should().updateBasicInfo("bg-key", List.of("https://new.link"), "new intro");
        then(updateExpertPort).should().updateExpert(expert);
        then(userUpdatePort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("User 정보만 수정되는 경우, User만 update 된다.")
    void updateExpertBasicInfo_onlyUserChanged() {
        // given
        String userNo = "USER002";
        String expertNo = "EXP002";

        UpdateExpertBasicInfoAppRequest request = UpdateExpertBasicInfoAppRequest.builder()
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

        given(userReadPort.findById(userNo)).willReturn(user);
        given(userReadPort.findUserInfoByExpertNo(expertNo)).willReturn(Optional.of(userWithProfile));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userReadPort).should().findById(userNo);
        then(userReadPort).should().findUserInfoByExpertNo(expertNo);
        then(userUpdatePort).should().updateNicknameAndImageByExpertNo(argThat(dto ->
                dto.nickname().equals("UpdatedNickname") &&
                        dto.profileImageFileKey().equals("updated-profile-img") &&
                        dto.expertNo().equals(expertNo)
        ));
        then(loadExpertPort).shouldHaveNoInteractions();
        then(updateExpertPort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("모든 값이 null일 경우 아무 업데이트도 수행되지 않는다.")
    void updateExpertBasicInfo_nothingChanged() {
        // given
        String userNo = "USER003";

        UpdateExpertBasicInfoAppRequest request = UpdateExpertBasicInfoAppRequest.builder()
                .currentUserNo(userNo)
                .build(); // 모든 필드가 null

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(userReadPort).shouldHaveNoInteractions();
        then(loadExpertPort).shouldHaveNoInteractions();
        then(updateExpertPort).shouldHaveNoInteractions();
        then(userUpdatePort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Expert 상세 정보가 모두 수정되는 경우 updateExpertWithDetail이 호출된다.")
    void updateExpertDetailInfo_success() {
        // given
        String userNo = "USR001";
        String expertNo = "EXP001";

        // --- 요청 DTO 구성
        ProjectCommand projectNew = createProjectCommand(null, "추가된 프로젝트",
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 11, 0), ChangeStatus.NEW);
        ProjectCommand projectUpdate = createProjectCommand("PRJ002", "수정된 프로젝트",
                LocalDateTime.of(2023, 12, 1, 10, 0),
                LocalDateTime.of(2023, 12, 1, 11, 0), ChangeStatus.UPDATE);
        ProjectCommand projectDelete = createProjectCommand("PRJ003", "삭제된 프로젝트",
                LocalDateTime.of(2022, 11, 1, 10, 0),
                LocalDateTime.of(2022, 11, 1, 11, 0), ChangeStatus.DELETE);

        SkillCommand skillNew = createSkillCommand(null, SkillType.LIGHT, "조명 가능", ChangeStatus.NEW);
        SkillCommand skillUpdate = createSkillCommand("SKILL002", SkillType.CAMERA, "카메라 전문가", ChangeStatus.UPDATE);
        SkillCommand skillDelete = createSkillCommand("SKILL003", SkillType.EDIT, "편집 불필요", ChangeStatus.DELETE);

        StudioCommand studioNew = createStudioCommand(null, "스튜디오 신규", 2, "09:00~18:00", "서울시 강남구", ChangeStatus.NEW);

        UpdateExpertDetailInfoAppRequest request = UpdateExpertDetailInfoAppRequest.builder()
                .currentUserNo(userNo)
                .activityCareer("5년 경력")
                .activityAreas(List.of("서울", "부산"))
                .projects(List.of(projectNew, projectUpdate, projectDelete))
                .skills(List.of(skillNew, skillUpdate, skillDelete))
                .studio(studioNew)
                .build();

        // --- Mock 정의
        User user = mock(User.class);
        given(userReadPort.findById(userNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(expertNo);

        Expert expert = mock(Expert.class);
        given(loadExpertPort.findById(expertNo)).willReturn(Optional.of(expert));

        Project pjNew = mock(Project.class);
        Project pjUpdate = mock(Project.class);
        given(projectCommandAppMapper.toDomain(projectNew)).willReturn(pjNew);
        given(projectCommandAppMapper.toDomain(projectUpdate)).willReturn(pjUpdate);

        Skill skNew = mock(Skill.class);
        Skill skUpdate = mock(Skill.class);
        given(skillCommandAppMapper.toDomain(skillNew)).willReturn(skNew);
        given(skillCommandAppMapper.toDomain(skillUpdate)).willReturn(skUpdate);

        Studio studio = mock(Studio.class);
        given(studioCommandAppMapper.toDomain(studioNew)).willReturn(studio);

        // when
        expertInfoCommandService.updateExpertDetailInfo(request);

        // then
        InOrder inOrder = inOrder(
                userReadPort, user,
                loadExpertPort, expert,
                projectCommandAppMapper, expert,
                skillCommandAppMapper, expert,
                studioCommandAppMapper, expert,
                updateExpertPort
        );

        then(userReadPort).should(inOrder).findById(userNo);
        then(user).should(inOrder).getExpertNo();
        then(loadExpertPort).should(inOrder).findById(expertNo);

        then(projectCommandAppMapper).should(inOrder).toDomain(projectNew);
        then(expert).should(inOrder).addProject(pjNew);
        then(projectCommandAppMapper).should(inOrder).toDomain(projectUpdate);
        then(expert).should(inOrder).updateProject(pjUpdate);
        then(expert).should(inOrder).deleteProject("PRJ003");

        then(skillCommandAppMapper).should(inOrder).toDomain(skillNew);
        then(expert).should(inOrder).addSkill(skNew);
        then(skillCommandAppMapper).should(inOrder).toDomain(skillUpdate);
        then(expert).should(inOrder).updateSkill(skUpdate);
        then(expert).should(inOrder).deleteSkill("SKILL003");

        then(studioCommandAppMapper).should(inOrder).toDomain(studioNew);
        then(expert).should(inOrder).addStudio(studio);

        then(updateExpertPort).should(inOrder).updateExpertWithDetail(
                eq(expert),
                eq(List.of("PRJ003")),
                eq(List.of("SKILL003")),
                eq(null)
        );

        then(updateExpertPort).shouldHaveNoMoreInteractions();
    }

    private ProjectCommand createProjectCommand(String projectNo, String projectName, LocalDateTime startDate, LocalDateTime endDate, ChangeStatus changeStatus) {
        return ProjectCommand.builder()
                .projectNo(projectNo)
                .projectName(projectName)
                .startDate(startDate)
                .endDate(endDate)
                .changeStatus(changeStatus)
                .build();
    }

    private SkillCommand createSkillCommand(String skillNo, SkillType skillType, String content, ChangeStatus changeStatus) {
        return SkillCommand.builder()
                .skillNo(skillNo)
                .skillType(skillType)
                .content(content)
                .changeStatus(changeStatus)
                .build();
    }

    private StudioCommand createStudioCommand(String studioNo, String studioName, int employeesCount, String businessHours, String address, ChangeStatus changeStatus) {
        return StudioCommand.builder()
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

        UpdateExpertDetailInfoAppRequest request = UpdateExpertDetailInfoAppRequest.builder()
                .currentUserNo(userNo)
                .build(); // 모든 필드가 null

        // when
        expertInfoCommandService.updateExpertDetailInfo(request);

        // then
        then(userReadPort).shouldHaveNoInteractions();
        then(loadExpertPort).shouldHaveNoInteractions();
        then(updateExpertPort).shouldHaveNoInteractions();
        then(userUpdatePort).shouldHaveNoInteractions();
    }
}