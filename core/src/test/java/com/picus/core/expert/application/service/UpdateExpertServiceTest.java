package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.command.*;
import com.picus.core.expert.application.port.in.mapper.UpdateProjectCommandMapper;
import com.picus.core.expert.application.port.in.mapper.UpdateSkillCommandMapper;
import com.picus.core.expert.application.port.in.mapper.UpdateStudioCommandMapper;
import com.picus.core.expert.application.port.out.ExpertReadPort;
import com.picus.core.expert.application.port.out.ExpertUpdatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.domain.vo.SkillType;
import com.picus.core.user.application.port.out.UserUpdatePort;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UpdateExpertServiceTest {

    @Mock
    UserReadPort userReadPort;
    @Mock
    UserUpdatePort userUpdatePort;
    @Mock
    ExpertReadPort expertReadPort;
    @Mock
    ExpertUpdatePort expertUpdatePort;

    @Mock
    UpdateProjectCommandMapper updateProjectCommandMapper;
    @Mock
    UpdateSkillCommandMapper updateSkillCommandMapper;
    @Mock
    UpdateStudioCommandMapper updateStudioCommandMapper;

    @InjectMocks
    UpdateExpertService expertInfoCommandService;

    @Test
    @DisplayName("Expert와 User 기본 정보가 모두 수정되는 경우 update 호출이 수행된다.")
    void updateExpertBasicInfo_success() {
        // given
        String userNo = "USER001";
        String expertNo = userNo; // pk를 서로 공유

        UpdateExpertBasicInfoCommand request = UpdateExpertBasicInfoCommand.builder()
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

        given(expertReadPort.findById(expertNo)).willReturn(Optional.of(expert));
        given(userReadPort.findUserInfoByExpertNo(expertNo)).willReturn(Optional.of(userWithProfile));

        // when
        expertInfoCommandService.updateExpertBasicInfo(request);

        // then
        then(expertReadPort).should().findById(expertNo);
        then(expert).should().updateBasicInfo("new-background", List.of("https://new.link"), "New intro");
        then(expertUpdatePort).should().update(expert);
        then(userReadPort).should().findUserInfoByExpertNo(expertNo);
        then(userUpdatePort).should().updateNicknameAndImageByExpertNo(argThat(updatedDto ->
                updatedDto.nickname().equals("NewNickname") &&
                        updatedDto.profileImageFileKey().equals("new-profile-img") &&
                        updatedDto.expertNo().equals(expertNo)
        ));
    }

    @Test
    @DisplayName("Expert 상세 정보가 모두 수정되는 경우 updateExpertWithDetail이 호출된다.")
    void updateExpertDetailInfo_success() {
        // given
        String userNo = "USR001";
        String expertNo = userNo; // PK 공유

        // --- 요청 DTO 구성
        UpdateProjectCommand projectNew = createProjectCommand(null, "추가된 프로젝트",
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 11, 0), ChangeStatus.NEW);
        UpdateProjectCommand projectUpdate = createProjectCommand("PRJ002", "수정된 프로젝트",
                LocalDateTime.of(2023, 12, 1, 10, 0),
                LocalDateTime.of(2023, 12, 1, 11, 0), ChangeStatus.UPDATE);
        UpdateProjectCommand projectDelete = createProjectCommand("PRJ003", "삭제된 프로젝트",
                LocalDateTime.of(2022, 11, 1, 10, 0),
                LocalDateTime.of(2022, 11, 1, 11, 0), ChangeStatus.DELETE);

        UpdateSkillCommand skillNew = createSkillCommand(null, SkillType.LIGHT, "조명 가능", ChangeStatus.NEW);
        UpdateSkillCommand skillUpdate = createSkillCommand("SKILL002", SkillType.CAMERA, "카메라 전문가", ChangeStatus.UPDATE);
        UpdateSkillCommand skillDelete = createSkillCommand("SKILL003", SkillType.EDIT, "편집 불필요", ChangeStatus.DELETE);

        UpdateStudioCommand studioNew = createStudioCommand(null, "스튜디오 신규", 2, "09:00~18:00", "서울시 강남구", ChangeStatus.NEW);

        UpdateExpertDetailInfoCommand request = UpdateExpertDetailInfoCommand.builder()
                .currentUserNo(userNo)
                .activityCareer("5년 경력")
                .activityAreas(List.of("서울", "부산"))
                .projects(List.of(projectNew, projectUpdate, projectDelete))
                .skills(List.of(skillNew, skillUpdate, skillDelete))
                .studio(studioNew)
                .build();

        // --- Mock 정의

        Expert expert = mock(Expert.class);
        given(expertReadPort.findById(expertNo)).willReturn(Optional.of(expert));

        Project pjNew = mock(Project.class);
        Project pjUpdate = mock(Project.class);
        given(updateProjectCommandMapper.toDomain(projectNew)).willReturn(pjNew);
        given(updateProjectCommandMapper.toDomain(projectUpdate)).willReturn(pjUpdate);

        Skill skNew = mock(Skill.class);
        Skill skUpdate = mock(Skill.class);
        given(updateSkillCommandMapper.toDomain(skillNew)).willReturn(skNew);
        given(updateSkillCommandMapper.toDomain(skillUpdate)).willReturn(skUpdate);

        Studio studio = mock(Studio.class);
        given(updateStudioCommandMapper.toDomain(studioNew)).willReturn(studio);

        // when
        expertInfoCommandService.updateExpertDetailInfo(request);

        // then
        InOrder inOrder = inOrder(
                expertReadPort, expert,
                updateProjectCommandMapper, expert,
                updateSkillCommandMapper, expert,
                updateStudioCommandMapper, expert,
                expertUpdatePort
        );

        then(expertReadPort).should(inOrder).findById(expertNo);

        then(updateProjectCommandMapper).should(inOrder).toDomain(projectNew);
        then(expert).should(inOrder).addProject(pjNew);
        then(updateProjectCommandMapper).should(inOrder).toDomain(projectUpdate);
        then(expert).should(inOrder).updateProject(pjUpdate);
        then(expert).should(inOrder).deleteProject("PRJ003");

        then(updateSkillCommandMapper).should(inOrder).toDomain(skillNew);
        then(expert).should(inOrder).addSkill(skNew);
        then(updateSkillCommandMapper).should(inOrder).toDomain(skillUpdate);
        then(expert).should(inOrder).updateSkill(skUpdate);
        then(expert).should(inOrder).deleteSkill("SKILL003");

        then(updateStudioCommandMapper).should(inOrder).toDomain(studioNew);
        then(expert).should(inOrder).addStudio(studio);

        then(expertUpdatePort).should(inOrder).update(
                eq(expert),
                eq(List.of("PRJ003")),
                eq(List.of("SKILL003")),
                eq(null)
        );

        then(expertUpdatePort).shouldHaveNoMoreInteractions();
    }

    private UpdateProjectCommand createProjectCommand(String projectNo, String projectName, LocalDateTime startDate, LocalDateTime endDate, ChangeStatus changeStatus) {
        return UpdateProjectCommand.builder()
                .projectNo(projectNo)
                .projectName(projectName)
                .startDate(startDate)
                .endDate(endDate)
                .changeStatus(changeStatus)
                .build();
    }

    private UpdateSkillCommand createSkillCommand(String skillNo, SkillType skillType, String content, ChangeStatus changeStatus) {
        return UpdateSkillCommand.builder()
                .skillNo(skillNo)
                .skillType(skillType)
                .content(content)
                .changeStatus(changeStatus)
                .build();
    }

    private UpdateStudioCommand createStudioCommand(String studioNo, String studioName, int employeesCount, String businessHours, String address, ChangeStatus changeStatus) {
        return UpdateStudioCommand.builder()
                .studioNo(studioNo)
                .studioName(studioName)
                .employeesCount(employeesCount)
                .businessHours(businessHours)
                .address(address)
                .changeStatus(changeStatus)
                .build();
    }
}