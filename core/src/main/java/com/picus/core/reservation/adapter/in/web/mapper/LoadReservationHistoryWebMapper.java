package com.picus.core.reservation.adapter.in.web.mapper;

import com.picus.core.reservation.adapter.in.web.data.response.LoadReservationHistoryResponse;
import com.picus.core.reservation.domain.Reservation;
import org.springframework.stereotype.Component;

@Component
public class LoadReservationHistoryWebMapper {

    public LoadReservationHistoryResponse toResponse(Reservation reservation) {
        return LoadReservationHistoryResponse.builder()
                .reservationNo(reservation.getReservationNo())
                .thumbnailImage(null)   // todo: image
//                .date(reservation.getStartTime())
                .reservationStatus(reservation.getReservationStatus().getDesc())
                .expertName()
                .build();
    }
}
