package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.request.UpdateProjectAppReq;
import com.picus.core.expert.domain.Project;
import org.springframework.stereotype.Component;

@Component
public class UpdateProjectAppMapper {

    public Project toDomain(UpdateProjectAppReq command) {
        return Project.builder()
                .projectNo(command.projectNo())
                .projectName(command.projectName())
                .startDate(command.startDate())
                .endDate(command.endDate())
                .build();
    }
}
