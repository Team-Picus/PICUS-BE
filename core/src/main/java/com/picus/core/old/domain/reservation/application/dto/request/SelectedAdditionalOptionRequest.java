package com.picus.core.old.domain.reservation.application.dto.request;

public record SelectedAdditionalOptionRequest(
        Long additionalOptionId,
        Integer count
) {}