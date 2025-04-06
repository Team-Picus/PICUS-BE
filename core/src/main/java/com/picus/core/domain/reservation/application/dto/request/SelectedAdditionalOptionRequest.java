package com.picus.core.domain.reservation.application.dto.request;

public record SelectedAdditionalOptionRequest(
        Long additionalOptionId,
        Integer count
) {}