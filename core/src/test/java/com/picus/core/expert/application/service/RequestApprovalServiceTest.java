package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.in.RequestApprovalRequest;
import com.picus.core.expert.application.port.out.SaveExpertPort;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ActivityArea;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.Portfolio;
import com.picus.core.expert.domain.model.vo.SkillType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class RequestApprovalServiceTest {

    private final SaveExpertPort saveExpertPort = Mockito.mock(SaveExpertPort.class);

    private final RequestApprovalService requestApprovalService
            = new RequestApprovalService(saveExpertPort);


    @Test
    @DisplayName("승인 요청시 ApprovalStatus가 PENDING인 Expert가 저장된다.")
    public void requestApproval_success() throws Exception {
        // given
        RequestApprovalRequest request = givenRequestApprovalRequest();

        // saveExpertPort.saveExpert() stubbing
        Expert savedExpert = givenSaveExpertPortResult(request);

        // when
        Expert result = requestApprovalService.requestApproval(request);

        // then
        ArgumentCaptor<Expert> captor = ArgumentCaptor.forClass(Expert.class);

        then(saveExpertPort).should().saveExpert(captor.capture()); // out port를 호출했는지 검증

        Expert captured = captor.getValue();
        assertThat(captured.getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING); // 적절한 파라미터를 사용했는지 검증

        assertThat(result).isEqualTo(savedExpert); // 리턴값을 잘 반환했는지 검증
    }

    private RequestApprovalRequest givenRequestApprovalRequest() {
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
                .activityAreas(List.of(ActivityArea.SEOUL_GANGBUKGU, ActivityArea.SEOUL_GANGDONGGU))
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
                .build();
    }

    private Expert givenSaveExpertPortResult(RequestApprovalRequest request) {
        Expert savedExpert = Expert.builder()
                .expertNo("expert_no1")
                .activityCareer(request.activityCareer())
                .approvalStatus(ApprovalStatus.PENDING)
                .projects(request.projects())
                .activityAreas(request.activityAreas())
                .skills(request.skills())
                .studio(request.studio())
                .portfolioLinks(request.portfolios())
                .build();

        given(saveExpertPort.saveExpert(any(Expert.class)))
                .willReturn(savedExpert);

        return savedExpert;
    }

}