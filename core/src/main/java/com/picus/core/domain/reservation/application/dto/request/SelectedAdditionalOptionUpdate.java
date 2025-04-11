package com.picus.core.domain.reservation.application.dto.request;

public record SelectedAdditionalOptionUpdate (
        Long selectedAdditionalOptionId,
        Integer count
) {}
