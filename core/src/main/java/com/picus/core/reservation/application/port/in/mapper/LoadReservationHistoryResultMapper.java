package com.picus.core.reservation.application.port.in.mapper;

import com.picus.core.reservation.application.port.in.response.LoadReservationHistoryResult;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class LoadReservationHistoryResultMapper {

    public LoadReservationHistoryResult toResult(Reservation reservation, String thumbnailImage, User user) {
        return LoadReservationHistoryResult.builder()
                .reservationNo(reservation.getReservationNo())
                .thumbnailImage(thumbnailImage)
                .reservationStatus(reservation.getReservationStatus().getDesc())
                .expertName(user.getName())
                .dateTime(reservation.getStartTime())
                .place(reservation.getPlace())
                .build();
    }

}
