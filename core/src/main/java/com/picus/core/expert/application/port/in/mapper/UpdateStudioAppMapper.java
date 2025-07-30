package com.picus.core.expert.application.port.in.mapper;

import com.picus.core.expert.application.port.in.request.UpdateStudioCommand;
import com.picus.core.expert.domain.Studio;
import org.springframework.stereotype.Component;

@Component
public class UpdateStudioAppMapper {

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