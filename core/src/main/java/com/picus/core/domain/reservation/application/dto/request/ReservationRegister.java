package com.picus.core.domain.reservation.application.dto.request;

import com.picus.core.domain.reservation.domain.entity.Schedule;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReservationRegister(
        @NotNull Long reservationId,
        @NotNull Schedule schedule,
        @NotNull String detail,
        List<SelectedAdditionalOptionRegister> selectedOptions
) {}
