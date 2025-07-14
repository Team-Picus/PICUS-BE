package com.picus.core.weekly_magazine.adapter.out.persistence.converter;

import com.picus.core.post.domain.model.vo.PostThemeType;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostThemeTypeConverter implements AttributeConverter<List<PostThemeType>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<PostThemeType> postThemeTypes) {
        if (postThemeTypes == null || postThemeTypes.isEmpty()) {
            return "";
        }
        return postThemeTypes.stream()
                .map(PostThemeType::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<PostThemeType> convertToEntityAttribute(String themeTypeString) {
        if (themeTypeString == null || themeTypeString.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(themeTypeString.split(SEPARATOR))
                .map(PostThemeType::valueOf)
                .collect(Collectors.toList());
    }
}
