package com.picus.core.global.common.converter;

import com.picus.core.domain.expert.domain.entity.ActivityType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class ActivityTypeSetConverter implements AttributeConverter<Set<ActivityType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<ActivityType> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        // enum의 이름을 사용하여 문자열로 변환
        return attribute.stream()
                .map(ActivityType::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<ActivityType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new HashSet<>();
        }
        // 저장된 enum 이름을 다시 ActivityType으로 변환
        return Arrays.stream(dbData.split(DELIMITER))
                .map(String::trim)
                .map(ActivityType::valueOf)
                .collect(Collectors.toSet());
    }
}
