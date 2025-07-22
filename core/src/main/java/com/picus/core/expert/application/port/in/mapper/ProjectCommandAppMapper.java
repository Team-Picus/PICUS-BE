package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.command.ProjectCommand;
import com.picus.core.expert.domain.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectCommandAppMapper {

    public Project toDomain(ProjectCommand command) {
        return Project.builder()
                .projectNo(command.projectNo())
                .projectName(command.projectName())
                .startDate(command.startDate())
                .endDate(command.endDate())
                .build();
    }
}
