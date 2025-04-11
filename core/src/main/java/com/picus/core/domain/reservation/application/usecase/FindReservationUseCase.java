package com.picus.core.domain.reservation.application.usecase;

import com.picus.core.domain.post.application.usecase.PostManagementUseCase;
import com.picus.core.domain.reservation.application.converter.ReservationConverter;
import com.picus.core.domain.reservation.application.dto.response.ReservationSummaryResponse;
import com.picus.core.domain.reservation.domain.entity.Reservation;
import com.picus.core.domain.reservation.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindReservationUseCase {

    private final ReservationService reservationService;
    private final PostManagementUseCase postManagementUseCase;

    public ReservationSummaryResponse getReservationSummary(Long reservationId) {
        Reservation reservation = reservationService.findById(reservationId);
        return ReservationConverter.convertToReservationSummaryResponse(reservation);
    }
}
