package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.adapter.out.persistence.mapper.ProjectPersistenceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectPersistenceMapperTest {

    private final ProjectPersistenceMapper mapper = new ProjectPersistenceMapper();

    @Test
    @DisplayName("ProjectEntity 객체를 Project 도메인으로 변환할 수 있다")
    void mapToDomain() {
        // Given
        ProjectEntity entity = ProjectEntity.builder()
                .projectName("AI 분석 프로젝트")
                .startDate(LocalDateTime.of(2024, 7, 1, 9, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 18, 0))
                .build();

        // When
        Project domain = mapper.mapToDomain(entity);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getProjectName()).isEqualTo("AI 분석 프로젝트");
        assertThat(domain.getStartDate()).isEqualTo(LocalDateTime.of(2024, 7, 1, 9, 0));
        assertThat(domain.getEndDate()).isEqualTo(LocalDateTime.of(2024, 12, 31, 18, 0));
    }

    @Test
    @DisplayName("Project 도메인 객체를 ProjectEntity로 변환할 수 있다")
    void mapToEntity() {
        // Given
        Project domain = Project.builder()
                .projectName("플랫폼 리뉴얼")
                .startDate(LocalDateTime.of(2025, 1, 1, 10, 0))
                .endDate(LocalDateTime.of(2025, 6, 30, 17, 0))
                .build();

        // When
        ProjectEntity entity = mapper.mapToEntity(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getProjectName()).isEqualTo("플랫폼 리뉴얼");
        assertThat(entity.getStartDate()).isEqualTo(LocalDateTime.of(2025, 1, 1, 10, 0));
        assertThat(entity.getEndDate()).isEqualTo(LocalDateTime.of(2025, 6, 30, 17, 0));
    }
}