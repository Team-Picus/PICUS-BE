package com.picus.core.reservation.adapter.in.web;

import com.picus.core.reservation.application.port.in.CompleteReservationUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompleteReservationController {

    private final CompleteReservationUseCase completeReservationUseCase;

    @PostMapping("/api/reservation/{reservationNo}/complete")
    public BaseResponse<Void> complete(
            @CurrentUser String userNo,
            @PathVariable String reservationNo
    ) {
        completeReservationUseCase.complete(userNo, reservationNo);
    }
}
