package com.picus.core.reservation.application.port.in;

import com.picus.core.reservation.application.port.in.response.LoadReservationDetailResult;
import com.picus.core.reservation.application.port.in.response.LoadReservationHistoryResult;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface LoadReservationHistoryUseCase {

    List<LoadReservationHistoryResult> loadAll(String userNo, LocalDateTime duration, ReservationStatus status);
    LoadReservationDetailResult load(String userNo, String reservationNo);

}
