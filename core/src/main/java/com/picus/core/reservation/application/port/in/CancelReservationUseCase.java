package com.picus.core.reservation.application.port.in;

public interface CancelReservationUseCase {

    void cancel(String userNo, String reservationNo);

}
