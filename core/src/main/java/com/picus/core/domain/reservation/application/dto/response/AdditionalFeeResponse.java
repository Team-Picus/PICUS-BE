package com.picus.core.domain.reservation.application.dto.response;

public record AdditionalFeeResponse(
        Long id,
        Integer feeAmount,
        String feeReason
) {
}