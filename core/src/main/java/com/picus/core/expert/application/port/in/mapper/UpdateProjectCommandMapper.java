package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.command.UpdateProjectCommand;
import com.picus.core.expert.domain.Project;
import org.springframework.stereotype.Component;

@Component
public class UpdateProjectCommandMapper {

    public Project toDomain(UpdateProjectCommand command) {
        return Project.builder()
                .projectNo(command.projectNo())
                .projectName(command.projectName())
                .startDate(command.startDate())
                .endDate(command.endDate())
                .build();
    }
}
