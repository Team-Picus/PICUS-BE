package com.picus.core.expert.adapter.out.persistence.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;

public class StringConverter implements AttributeConverter<List<String>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> linkList) {
        if (linkList == null || linkList.isEmpty()) {
            return "";
        }
        return String.join(SEPARATOR, linkList);
    }

    @Override
    public List<String> convertToEntityAttribute(String linkString) {
        if (linkString == null || linkString.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(linkString.split(SEPARATOR))
                .map(String::trim)
                .toList();
    }
}
