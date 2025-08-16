package com.picus.core.reservation.application.service;

import com.picus.core.reservation.adapter.out.persistence.repository.ReservationBlacklistRepository;
import com.picus.core.reservation.application.port.in.CancelReservationUseCase;
import com.picus.core.reservation.application.port.out.ReservationBlacklistCreatePort;
import com.picus.core.reservation.application.port.out.ReservationBlacklistReadPort;
import com.picus.core.reservation.application.port.out.ReservationDeletePort;
import com.picus.core.reservation.application.port.out.ReservationReadPort;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.ALREADY_COMPLETED_RESERVATION;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.RESERVATION_OWNER_MISMATCH;

@Service
@RequiredArgsConstructor
public class CancelReservationService implements CancelReservationUseCase {

    private final ReservationReadPort reservationReadPort;
    private final ReservationBlacklistCreatePort reservationBlacklistCreatePort;
    private final ReservationDeletePort reservationDeletePort;

    @Override
    public void cancel(String userNo, String reservationNo) {
        Reservation reservation = reservationReadPort.findById(reservationNo);

        if (reservation.isClient(userNo))
            throw new RestApiException(RESERVATION_OWNER_MISMATCH);

        if (reservation.isCompleted())
            throw new RestApiException(ALREADY_COMPLETED_RESERVATION);

        if (reservation.isApproval()) {
            reservationBlacklistCreatePort.create(userNo);
        } else if (reservation.isPaid()) {
            // 결제 취소
        }

        reservationDeletePort.delete(reservationNo);
    }
}
