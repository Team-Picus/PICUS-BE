package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.command.UpdateStudioCommand;
import com.picus.core.expert.domain.Studio;
import org.springframework.stereotype.Component;

@Component
public class UpdateStudioCommandMapper {

    public Studio toDomain(UpdateStudioCommand command) {
        return Studio.builder()
                .studioNo(command.studioNo())
                .studioName(command.studioName())
                .employeesCount(command.employeesCount())
                .businessHours(command.businessHours())
                .address(command.address())
                .build();
    }

}