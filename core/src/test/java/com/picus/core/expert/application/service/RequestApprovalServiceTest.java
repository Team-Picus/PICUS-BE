package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.command.RequestApprovalCommand;
import com.picus.core.expert.application.port.in.mapper.RequestApprovalCommandMapper;
import com.picus.core.expert.application.port.out.ExpertCreatePort;
import com.picus.core.expert.domain.Expert;
import com.picus.core.expert.domain.Project;
import com.picus.core.expert.domain.Skill;
import com.picus.core.expert.domain.Studio;
import com.picus.core.expert.domain.vo.SkillType;
import com.picus.core.user.application.port.out.UserUpdatePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class RequestApprovalServiceTest {

    final ExpertCreatePort expertCreatePort = Mockito.mock(ExpertCreatePort.class);
    final UserUpdatePort userUpdatePort = Mockito.mock(UserUpdatePort.class);
    final RequestApprovalCommandMapper appMapper = new RequestApprovalCommandMapper();

    private final RequestApprovalService requestApprovalService
            = new RequestApprovalService(expertCreatePort, userUpdatePort, appMapper);


    @Test
    @DisplayName("전문가 승인요청 메서드 상호작용 검증")
    public void requestApproval_success() throws Exception {
        // given
        RequestApprovalCommand command = givenRequestApprovalCommand();
        stubOutPortMethod();

        // when
        requestApprovalService.requestApproval(command);

        // then

        then(expertCreatePort).should().create(any(Expert.class), any(String.class)); // out port를 호출했는지 검증
        then(userUpdatePort).should().assignExpertNo(any(String.class), any(String.class));
    }



    private RequestApprovalCommand givenRequestApprovalCommand() {
        return RequestApprovalCommand.builder()
                .activityCareer("3년차")
                .projects(List.of(
                        Project.builder()
                                .projectName("단편영화 촬영 프로젝트")
                                .startDate(LocalDateTime.of(2022, 5, 1, 0, 0))
                                .endDate(LocalDateTime.of(2022, 8, 15, 0, 0))
                                .build(),
                        Project.builder()
                                .projectName("뮤직비디오 조명 작업")
                                .startDate(LocalDateTime.of(2023, 1, 10, 0, 0))
                                .endDate(LocalDateTime.of(2023, 2, 20, 0, 0))
                                .build()
                ))
                .activityAreas(List.of("서울 강북구", "서울 강동구"))
                .skills(List.of(
                        Skill.builder()
                                .skillType(SkillType.CAMERA)
                                .content("시네마 카메라 운용 가능 (RED, Blackmagic)")
                                .build(),
                        Skill.builder()
                                .skillType(SkillType.EDIT)
                                .content("프리미어 프로 및 다빈치 리졸브 활용 편집 가능")
                                .build()
                ))
                .studio(Studio.builder()
                        .studioName("크리에이티브 필름")
                        .employeesCount(5)
                        .businessHours("10:00 - 19:00")
                        .address("서울특별시 마포구 월드컵북로 400")
                        .build())
                .portfolioLinks(List.of(
                        "https://myportfolio.com/project1",
                        "https://myportfolio.com/project2"
                ))
                .userNo("user_no1")
                .build();
    }
    private void stubOutPortMethod() {
        Expert expert = Expert.builder()
                .expertNo("expert_no1")
                .build();
        given(expertCreatePort.create(any(Expert.class), any(String.class)))
                .willReturn(expert);
    }

}
