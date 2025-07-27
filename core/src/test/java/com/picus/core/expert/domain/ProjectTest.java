package com.picus.core.expert.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest {

    @Test
    @DisplayName("모든 필드를 업데이트한다.")
    void updateProject_allFields() {
        // given
        Project project = Project.builder()
                .projectNo("P-001")
                .projectName("Old Name")
                .startDate(LocalDateTime.of(2024, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 0, 0))
                .build();

        // when
        project.updateProject(
                "New Name",
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 12, 31, 0, 0)
        );

        // then
        assertThat(project.getProjectName()).isEqualTo("New Name");
        assertThat(project.getStartDate()).isEqualTo(LocalDateTime.of(2025, 1, 1, 0, 0));
        assertThat(project.getEndDate()).isEqualTo(LocalDateTime.of(2025, 12, 31, 0, 0));
    }

    @Test
    @DisplayName("null 필드는 무시하고 나머지만 업데이트한다.")
    void updateProject_partialUpdate() {
        // given
        Project project = Project.builder()
                .projectNo("P-002")
                .projectName("Initial Name")
                .startDate(LocalDateTime.of(2023, 5, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 12, 1, 0, 0))
                .build();

        // when
        project.updateProject(null, null, LocalDateTime.of(2024, 1, 1, 0, 0));

        // then
        assertThat(project.getProjectName()).isEqualTo("Initial Name"); // 그대로 유지
        assertThat(project.getStartDate()).isEqualTo(LocalDateTime.of(2023, 5, 1, 0, 0)); // 그대로 유지
        assertThat(project.getEndDate()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0)); // 변경됨
    }
}