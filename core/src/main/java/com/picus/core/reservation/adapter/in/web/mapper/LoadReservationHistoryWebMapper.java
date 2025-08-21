package com.picus.core.reservation.adapter.in.web.mapper;

import com.picus.core.reservation.adapter.in.web.data.response.LoadReservationHistoryResponse;
import com.picus.core.reservation.application.port.in.response.LoadReservationHistoryResult;
import org.springframework.stereotype.Component;

@Component
public class LoadReservationHistoryWebMapper {

    public LoadReservationHistoryResponse toResponse(LoadReservationHistoryResult result) {
        return LoadReservationHistoryResponse.builder()
                .reservationNo(result.reservationNo())
                .thumbnailImage(result.thumbnailImage())
                .dateTime(result.dateTime())
                .reservationStatus(result.reservationStatus())
                .expertName(result.expertName())
                .place(result.place())
                .build();
    }
}
