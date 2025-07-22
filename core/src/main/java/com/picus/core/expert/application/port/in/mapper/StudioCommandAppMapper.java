package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.command.StudioCommand;
import com.picus.core.expert.domain.model.Studio;
import org.springframework.stereotype.Component;

@Component
public class StudioCommandAppMapper {

    public Studio toDomain(StudioCommand command) {
        return Studio.builder()
                .studioNo(command.studioNo())
                .studioName(command.studioName())
                .employeesCount(command.employeesCount())
                .businessHours(command.businessHours())
                .address(command.address())
                .build();
    }

}