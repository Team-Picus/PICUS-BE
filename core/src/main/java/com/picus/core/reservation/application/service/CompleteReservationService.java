package com.picus.core.reservation.application.service;

import com.picus.core.reservation.application.port.in.CompleteReservationUseCase;
import com.picus.core.reservation.application.port.out.ReservationReadPort;
import com.picus.core.reservation.application.port.out.ReservationUpdatePort;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.ReservationStatus;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.reservation.domain.ReservationStatus.*;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.RESERVATION_OWNER_MISMATCH;

@Service
@Transactional
@RequiredArgsConstructor
public class CompleteReservationService implements CompleteReservationUseCase {

    private final ReservationReadPort reservationReadPort;
    private final ReservationUpdatePort reservationUpdatePort;

    @Override
    public void complete(String userNo, String reservationNo) {
        Reservation reservation = reservationReadPort.findById(reservationNo);

        if(!reservation.isClient(userNo))
            throw new RestApiException(RESERVATION_OWNER_MISMATCH);

        reservationUpdatePort.update(reservationNo, COMPLETED);
    }
}
