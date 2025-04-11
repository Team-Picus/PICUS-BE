package com.picus.core.domain.reservation.application.dto.response;

public record SelectedAdditionalOptionResponse(
        Long id,
        Long additionalOptionNo,
        Integer count
) {
}
