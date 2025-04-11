package com.picus.core.domain.reservation.application.dto.response;

import java.util.List;

public record ReservationDetailResponse(
        ReservationSummaryResponse summary,
        SelectedOptionResponse selectedOption,
        List<AdditionalFeeResponse> additionalFees,
        int totalPrice
) {
}
