package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.request.ChangeStatus;
import com.picus.core.expert.application.port.in.request.UpdateProjectAppReq;
import com.picus.core.expert.domain.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateProjectAppRequestAppMapperTest {

    private final UpdateProjectAppMapper mapper = new UpdateProjectAppMapper();

    @Test
    @DisplayName("UpdateProjectAppReq 를 Project 도메인 객체로 변환한다.")
    void toDomain_success() {
        // given
        LocalDateTime now = LocalDateTime.now();
        UpdateProjectAppReq command = UpdateProjectAppReq.builder()
                .projectNo("PRJ-001")
                .projectName("뷰티 프로젝트")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .changeStatus(ChangeStatus.NEW)
                .build();

        // when
        Project project = mapper.toDomain(command);

        // then
        assertThat(project.getProjectNo()).isEqualTo("PRJ-001");
        assertThat(project.getProjectName()).isEqualTo("뷰티 프로젝트");
        assertThat(project.getStartDate()).isEqualTo(now.minusDays(1));
        assertThat(project.getEndDate()).isEqualTo(now.plusDays(1));
    }
}