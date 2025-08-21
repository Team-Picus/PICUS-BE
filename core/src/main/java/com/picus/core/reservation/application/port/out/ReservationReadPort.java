package com.picus.core.reservation.application.port.out;

import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationReadPort {

    Reservation findById(String reservationNo);

    List<Reservation> findByUserNoWithFilter(String userNo, LocalDateTime duration, ReservationStatus status);
}
