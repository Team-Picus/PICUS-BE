package com.picus.core.reservation.application.service;

import com.picus.core.reservation.application.port.in.DecideReservationRequestUseCase;
import com.picus.core.reservation.application.port.out.ReservationReadPort;
import com.picus.core.reservation.application.port.out.ReservationUpdatePort;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.picus.core.reservation.domain.ReservationStatus.APPROVAL;
import static com.picus.core.reservation.domain.ReservationStatus.REJECTED;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.ALREADY_DECIDE_RESERVATION;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.RESERVATION_OWNER_MISMATCH;

@Service
@RequiredArgsConstructor
public class DecideReservationRequestService implements DecideReservationRequestUseCase {

    private final ReservationUpdatePort reservationUpdatePort;
    private final ReservationReadPort reservationReadPort;

    @Override
    public void approval(String expertNo, String reservationNo) {
        validateReservation(expertNo, reservationNo);
        reservationUpdatePort.update(reservationNo, APPROVAL);
    }

    @Override
    public void reject(String expertNo, String reservationNo) {
        validateReservation(expertNo, reservationNo);
        reservationUpdatePort.update(reservationNo, REJECTED);
    }

    private void validateReservation(String expertNo, String reservationNo) {
        Reservation reservation = reservationReadPort.findById(reservationNo);

        if(!reservation.isExpert(expertNo))
            throw new RestApiException(RESERVATION_OWNER_MISMATCH);
        if(!reservation.isPending())
            throw new RestApiException(ALREADY_DECIDE_RESERVATION);
    }
}
