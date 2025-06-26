package com.picus.core.old.domain.post.application.dto.response;

public record AdditionalOptionResponse(
        Long id,
        String name,
        Integer pricePerUnit,
        Integer max,
        Integer base,
        Integer increment
) {}