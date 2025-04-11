package com.picus.core.domain.reservation.application.converter;

import com.picus.core.domain.reservation.application.dto.response.ReservationSummaryResponse;
import com.picus.core.domain.reservation.domain.entity.Reservation;

public abstract class ReservationConverter {

    public static ReservationSummaryResponse convertToReservationSummaryResponse(Reservation reservation) {
        return new ReservationSummaryResponse(
                reservation.getId(),
                reservation.getSchedule(),
                reservation.getDetail(),
                reservation.getClientId(),
                reservation.getPostId(),
                reservation.getStatus()
        );
    }
}
