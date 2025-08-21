package com.picus.core.reservation.application.port.out;

import com.picus.core.reservation.domain.Reservation;

public interface ReservationCreatePort {

    void create(Reservation reservation);

}
