package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.command.RequestApprovalRequest;
import com.picus.core.expert.domain.model.Expert;
import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.domain.model.Skill;
import com.picus.core.expert.domain.model.Studio;
import com.picus.core.expert.domain.model.vo.ApprovalStatus;
import com.picus.core.expert.domain.model.vo.Portfolio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestApprovalAppMapperTest {

    private final RequestApprovalAppMapper mapper = new RequestApprovalAppMapper();

    @Test
    @DisplayName("RequestApprovalCommand를 Expert 도메인 객체로 변환한다")
    void toDomain_shouldMapCorrectly() {
        // given
        RequestApprovalRequest command = RequestApprovalRequest.builder()
                .activityCareer("백엔드 5년차")
                .activityAreas(List.of("서울", "대전"))
                .projects(List.of(Project.builder().projectName("프로젝트A").build()))
                .skills(List.of(Skill.builder().content("Java").build()))
                .studio(Studio.builder().studioName("스튜디오A").build())
                .portfolios(List.of(Portfolio.builder().link("https://portfolio.site").build()))
                .userNo("user-001")
                .build();

        // when
        Expert expert = mapper.toDomain(command);

        // then
        assertThat(expert.getActivityCareer()).isEqualTo("백엔드 5년차");
        assertThat(expert.getActivityAreas()).containsExactly("서울", "대전");
        assertThat(expert.getProjects()).hasSize(1);
        assertThat(expert.getSkills()).extracting("content").contains("Java");
        assertThat(expert.getStudio().getStudioName()).isEqualTo("스튜디오A");
        assertThat(expert.getPortfolios()).hasSize(1);
        assertThat(expert.getPortfolios().get(0).getLink()).isEqualTo("https://portfolio.site");

        // 기본 초기값 확인
        assertThat(expert.getActivityCount()).isEqualTo(0);
        assertThat(expert.getApprovalStatus()).isEqualTo(ApprovalStatus.PENDING);
    }
}