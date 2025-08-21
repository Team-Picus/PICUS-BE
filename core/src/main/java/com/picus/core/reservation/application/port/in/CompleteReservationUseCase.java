package com.picus.core.reservation.application.port.in;

public interface CompleteReservationUseCase {

    void complete(String userNo, String reservationNo);

}
