package com.picus.core.domain.reservation.application.dto.request;

public record SelectedAdditionalOptionRegister(
        Long additionalOptionId,
        Integer count
) {}