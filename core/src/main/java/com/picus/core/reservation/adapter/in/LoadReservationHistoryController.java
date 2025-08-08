package com.picus.core.reservation.adapter.in;

import com.picus.core.reservation.adapter.in.web.data.response.LoadReservationHistoryResponse;
import com.picus.core.reservation.application.port.in.LoadReservationHistoryUseCase;
import com.picus.core.reservation.application.port.in.response.LoadReservationHistoryResult;
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

    @GetMapping
    public BaseResponse<List<LoadReservationHistoryResponse>> loadAll(
            @CurrentUser String userNo,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) ReservationStatus status
    ) {
        List<LoadReservationHistoryResult> reservations = loadReservationHistoryUseCase.loadAll(userNo, start, status);
        return BaseResponse.onSuccess(null);
    }

    @GetMapping("/{reservationNo}")
    public BaseResponse<Void> load(
            @PathVariable String reservationNo
    ) {
        return BaseResponse.onSuccess();
    }
}
