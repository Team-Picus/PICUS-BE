package com.picus.core.domain.reservation.application.dto.request;

import java.util.List;

public record ReservationUpdate(
        Long reservationId,
        List<SelectedAdditionalOptionUpdate> selectedAdditionalOptions,
        List<AdditionalFeeRegister> additionalFeeRegisters
) {
}
