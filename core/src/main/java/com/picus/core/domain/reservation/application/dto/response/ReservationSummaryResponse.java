package com.picus.core.domain.reservation.application.dto.response;

import com.picus.core.domain.reservation.domain.entity.ReservationStatus;
import com.picus.core.domain.reservation.domain.entity.Schedule;

public record ReservationSummaryResponse(
        Long id,
        Schedule schedule,
        String detail,
        Long clientId,
        Long postId,
        ReservationStatus status
) {}