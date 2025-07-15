package com.picus.core.expert.application.service;

import com.picus.core.expert.application.port.out.LoadExpertPort;
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
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

class GetExpertServiceTest {

    private final LoadExpertPort loadExpertPort = Mockito.mock(LoadExpertPort.class);

    private final GetExpertService getExpertService = new GetExpertService(loadExpertPort);

    @Test
    @DisplayName("특정 아이디를 가진 Expert를 조회한다.")
    public void getExpertInfo_success() throws Exception {
        // given
        String expertNo = "expertNo1";
        stubLoadExpertPortResult(expertNo);

        // when
        getExpertService.getExpertInfo(expertNo);

        // then
        then(loadExpertPort).should()
                .loadExpertByExpertNo(eq(expertNo));
    }

    private void stubLoadExpertPortResult(String expertNo) {
        Expert expert = givenExpertDomain();

        given(loadExpertPort.loadExpertByExpertNo(expertNo))
                .willReturn(Optional.of(expert));
    }

    private Expert givenExpertDomain() {
        return Expert.builder()
                .intro("소개입니다")
                .activityCareer("5년")
                .activityAreas(List.of(ActivityArea.SEOUL_GANGBUKGU))
                .activityCount(10)
                .lastActivityAt(LocalDateTime.of(2024, 5, 10, 10, 0))
                .portfolios(List.of(Portfolio.builder().link("http://portfolio.com").build()))
                .approvalStatus(ApprovalStatus.PENDING)
                .projects(List.of(
                        Project.builder()
                                .projectName("프로젝트 A")
                                .startDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                                .endDate(LocalDateTime.of(2023, 12, 31, 18, 0))
                                .build()
                ))
                .skills(List.of(
                        Skill.builder()
                                .skillType(SkillType.CAMERA)
                                .content("카메라 전문가")
                                .build()
                ))
                .studio(
                        Studio.builder()
                                .studioName("포토 스튜디오")
                                .employeesCount(3)
                                .businessHours("09:00~18:00")
                                .address("서울 강남구")
                                .build()
                )
                .build();
    }

}