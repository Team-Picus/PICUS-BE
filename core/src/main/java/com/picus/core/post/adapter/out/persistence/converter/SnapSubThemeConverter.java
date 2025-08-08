package com.picus.core.post.adapter.out.persistence.converter;

import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SnapSubThemeConverter implements AttributeConverter<List<SnapSubTheme>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<SnapSubTheme> snapSubThemes) {
        if (snapSubThemes == null || snapSubThemes.isEmpty()) {
            return "";
        }
        return snapSubThemes.stream()
                .map(SnapSubTheme::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<SnapSubTheme> convertToEntityAttribute(String snapSubThemeString) {
        if (snapSubThemeString == null || snapSubThemeString.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(snapSubThemeString.split(SEPARATOR))
                .map(SnapSubTheme::valueOf)
                .collect(Collectors.toList());
    }
}
