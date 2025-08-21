package com.picus.core.reservation.adapter.in;

import com.picus.core.reservation.adapter.in.web.data.response.LoadReservationDetailResponse;
import com.picus.core.reservation.adapter.in.web.data.response.LoadReservationDetailWebMapper;
import com.picus.core.reservation.adapter.in.web.data.response.LoadReservationHistoryResponse;
import com.picus.core.reservation.adapter.in.web.mapper.LoadReservationHistoryWebMapper;
import com.picus.core.reservation.application.port.in.LoadReservationHistoryUseCase;
import com.picus.core.reservation.application.port.in.response.LoadReservationDetailResult;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.ReservationStatus;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class LoadReservationHistoryController {

    private final LoadReservationHistoryUseCase loadReservationHistoryUseCase;
    private final LoadReservationHistoryWebMapper loadReservationHistoryWebMapper;
    private final LoadReservationDetailWebMapper loadReservationDetailWebMapper;

    @GetMapping
    public BaseResponse<List<LoadReservationHistoryResponse>> loadAll(
            @CurrentUser String userNo,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) ReservationStatus status
    ) {
        List<LoadReservationHistoryResponse> responses = loadReservationHistoryUseCase.loadAll(userNo, start, status).stream()
                .map(loadReservationHistoryWebMapper::toResponse)
                .toList();

        return BaseResponse.onSuccess(responses);
    }

    @GetMapping("/{reservationNo}")
    public BaseResponse<LoadReservationDetailResponse> load(
            @CurrentUser String userNo,
            @PathVariable String reservationNo
    ) {
        LoadReservationDetailResult reservation = loadReservationHistoryUseCase.load(userNo, reservationNo);
        return BaseResponse.onSuccess(loadReservationDetailWebMapper.toResponse(reservation));
    }
}
