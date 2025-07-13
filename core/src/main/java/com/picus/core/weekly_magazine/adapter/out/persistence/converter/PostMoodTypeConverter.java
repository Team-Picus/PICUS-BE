package com.picus.core.weekly_magazine.adapter.out.persistence.converter;

import com.picus.core.post.domain.model.vo.PostMoodType;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostMoodTypeConverter implements AttributeConverter<List<PostMoodType>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<PostMoodType> postMoodTypes) {
        if (postMoodTypes == null || postMoodTypes.isEmpty()) {
            return "";
        }
        return postMoodTypes.stream()
                .map(PostMoodType::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<PostMoodType> convertToEntityAttribute(String moodTypeString) {
        if (moodTypeString == null || moodTypeString.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(moodTypeString.split(SEPARATOR))
                .map(PostMoodType::valueOf)
                .collect(Collectors.toList());
    }
}
