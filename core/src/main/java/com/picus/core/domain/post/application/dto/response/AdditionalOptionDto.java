package com.picus.core.domain.post.application.dto.response;

public record AdditionalOptionDto(
        Long id,
        String name,
        Integer pricePerUnit,
        Integer max,
        Integer base,
        Integer increment
) {}