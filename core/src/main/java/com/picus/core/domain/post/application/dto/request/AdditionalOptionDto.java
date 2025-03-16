package com.picus.core.domain.post.application.dto.request;

public record AdditionalOptionDto(
        String name,
        Integer pricePerUnit,
        Integer max,
        Integer base,
        Integer increment
) {}