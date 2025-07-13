package com.picus.core.expert.infra.adapter.out.persistence.mapper;

import com.picus.core.expert.domain.model.Project;
import com.picus.core.expert.infra.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectPersistenceMapper {

    public Project mapToDomain(ProjectEntity entity) {
        return Project.builder()
                .projectName(entity.getProjectName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    public ProjectEntity mapToEntity(Project project) {

        return ProjectEntity.builder()
                .projectName(project.getProjectName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .build();
    }
}
