package com.picus.core.expert.infra.adapter.out.persistence.converter;

import com.picus.core.expert.domain.model.vo.ActivityArea;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityAreasConverter implements AttributeConverter<List<ActivityArea>, String> {
    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<ActivityArea> activityAreas) {
        if (activityAreas == null || activityAreas.isEmpty()) {
            return "";
        }
        return activityAreas.stream()
                .map(ActivityArea::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<ActivityArea> convertToEntityAttribute(String activityAreaString) {
        if (activityAreaString == null || activityAreaString.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(activityAreaString.split(SEPARATOR))
                .map(ActivityArea::valueOf)
                .collect(Collectors.toList());
    }
}
