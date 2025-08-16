package com.picus.core.reservation.application.port.in.mapper;

import com.picus.core.reservation.application.port.in.response.LoadReservationDetailResult;
import com.picus.core.reservation.domain.Reservation;
import org.springframework.stereotype.Component;

@Component
public class LoadReservationDetailResultMapper {

    public LoadReservationDetailResult toResult(Reservation reservation, String thumbnailImage) {
        return LoadReservationDetailResult.builder()
                .reservation(reservation)
                .thumbnailImage(thumbnailImage)
                .build();
    }

}
