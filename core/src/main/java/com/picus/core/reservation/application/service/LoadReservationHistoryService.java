package com.picus.core.reservation.application.service;

import com.picus.core.reservation.adapter.in.web.data.response.LoadReservationHistoryResponse;
import com.picus.core.reservation.application.port.in.LoadReservationHistoryUseCase;
import com.picus.core.reservation.application.port.in.response.LoadReservationHistoryResult;
import com.picus.core.reservation.application.port.out.ReservationReadPort;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadReservationHistoryService implements LoadReservationHistoryUseCase {

    private final ReservationReadPort reservationReadPort;

    @Override
    public List<LoadReservationHistoryResult> loadAll(String userNo, LocalDateTime start, ReservationStatus status) {
        List<Reservation> reservations = reservationReadPort.findByUserNoWithFilter(userNo, start, status);
        return null;
    }
}
