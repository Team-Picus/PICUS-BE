package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.command.RequestApprovalRequest;
import com.picus.core.expert.application.port.in.mapper.RequestApprovalAppMapper;
import com.picus.core.expert.application.port.out.CreateExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.domain.model.vo.SkillType;
import com.picus.core.user.application.port.out.UserCommandPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class RequestApprovalServiceTest {

    final CreateExpertPort createExpertPort = Mockito.mock(CreateExpertPort.class);
    final UserCommandPort userCommandPort = Mockito.mock(UserCommandPort.class);
    final RequestApprovalAppMapper appMapper = new RequestApprovalAppMapper();

    private final RequestApprovalService requestApprovalService
            = new RequestApprovalService(createExpertPort, userCommandPort, appMapper);


    @Test
    @DisplayName("전문가 승인요청 메서드 상호작용 검증")
    public void requestApproval_success() throws Exception {
        // given
        RequestApprovalRequest command = givenRequestApprovalCommand();
        stubOutPortMethod();

        // when
        requestApprovalService.requestApproval(command);

        // then

        then(createExpertPort).should().saveExpert(any(Expert.class), any(String.class)); // out port를 호출했는지 검증
        then(userCommandPort).should().assignExpertNo(any(String.class), any(String.class));
    }



    private RequestApprovalRequest givenRequestApprovalCommand() {
        return RequestApprovalRequest.builder()
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
                .portfolios(List.of(
                        Portfolio.builder()
                                .link("https://myportfolio.com/project1")
                                .build(),
                        Portfolio.builder()
                                .link("https://myportfolio.com/project2")
                                .build()
                ))
                .userNo("user_no1")
                .build();
    }
    private void stubOutPortMethod() {
        Expert expert = Expert.builder()
                .expertNo("expert_no1")
                .build();
        given(createExpertPort.saveExpert(any(Expert.class), any(String.class)))
                .willReturn(expert);
    }

}
