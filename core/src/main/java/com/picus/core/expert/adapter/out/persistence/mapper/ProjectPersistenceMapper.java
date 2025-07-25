package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.adapter.out.persistence.entity.ProjectEntity;
import com.picus.core.expert.domain.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectPersistenceMapper {

    public Project mapToDomain(ProjectEntity entity) {
        return Project.builder()
                .projectNo(entity.getProjectNo())
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
