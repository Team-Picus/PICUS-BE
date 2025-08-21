package com.picus.core.reservation.application.port.in;

public interface DecideReservationRequestUseCase {

    void approval(String userNo, String reservationNo);
    void reject(String userNo, String reservationNo);

}
