package com.picus.core.domain.reservation.application.dto.response;

import java.util.List;

public record SelectedOptionResponse(
        Long id,
        Integer basePrice,
        List<SelectedAdditionalOptionResponse> additionalOptions
) {
}