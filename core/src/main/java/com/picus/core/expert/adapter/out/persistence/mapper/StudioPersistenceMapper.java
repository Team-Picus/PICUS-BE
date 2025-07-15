package com.picus.core.expert.adapter.out.persistence.mapper;

import com.picus.core.expert.adapter.out.persistence.entity.StudioEntity;
import com.picus.core.expert.domain.model.Studio;
import org.springframework.stereotype.Component;

@Component
public class StudioPersistenceMapper {

    public Studio mapToDomain(StudioEntity entity) {
        return Studio.builder()
                .studioName(entity.getStudioName())
                .employeesCount(entity.getEmployeesCount())
                .businessHours(entity.getBusinessHours())
                .address(entity.getAddress())
                .build();
    }

    public StudioEntity mapToEntity(Studio studio) {
        return StudioEntity.builder()
                .studioName(studio.getStudioName())
                .employeesCount(studio.getEmployeesCount())
                .businessHours(studio.getBusinessHours())
                .address(studio.getAddress())
                .build();
    }
}
