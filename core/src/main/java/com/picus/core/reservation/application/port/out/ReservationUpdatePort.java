package com.picus.core.reservation.application.port.out;

import com.picus.core.reservation.domain.ReservationStatus;

public interface ReservationUpdatePort {

    void update(String reservationNo, ReservationStatus status);

}
