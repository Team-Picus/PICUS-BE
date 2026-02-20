package com.picus.core.reservation.application.port.in;

import com.picus.core.reservation.application.port.in.request.SaveReservationCommand;

public interface SendReservationRequestUseCase {

    void send(String userNo, SaveReservationCommand command);

}
