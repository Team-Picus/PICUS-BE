package com.picus.core.reservation.application.service;

import com.picus.core.reservation.adapter.in.web.data.response.LoadReservationHistoryResponse;
import com.picus.core.reservation.application.port.in.LoadReservationHistoryUseCase;
import com.picus.core.reservation.application.port.in.mapper.LoadReservationHistoryResultMapper;
import com.picus.core.reservation.application.port.in.response.LoadReservationHistoryResult;
import com.picus.core.reservation.application.port.out.ReservationReadPort;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.ReservationStatus;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadReservationHistoryService implements LoadReservationHistoryUseCase {

    private final ReservationReadPort reservationReadPort;
    private final UserReadPort userReadPort;
    private final LoadReservationHistoryResultMapper loadReservationHistoryResultMapper;

    @Override
    public List<LoadReservationHistoryResult> loadAll(String userNo, LocalDateTime start, ReservationStatus status) {
        return reservationReadPort.findByUserNoWithFilter(userNo, start, status).stream()
                .map(reservation -> {
                    User user = userReadPort.findById(reservation.getExpertNo());
                    String thumbnailImage = null;   // todo: Get Image
                    return loadReservationHistoryResultMapper.toResult(reservation, thumbnailImage, user);
                })
                .toList();
    }
}
