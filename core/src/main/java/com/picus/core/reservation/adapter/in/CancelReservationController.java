package com.picus.core.reservation.adapter.in;

import com.picus.core.reservation.application.port.in.CancelReservationUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CancelReservationController {

    private final CancelReservationUseCase cancelReservationUseCase;

    @DeleteMapping("/api/reservation/{reservationNo}")
    public BaseResponse<Void> cancel(
            @CurrentUser String userNo,
            @PathVariable String reservationNo
    ) {
        cancelReservationUseCase.cancel(userNo, reservationNo);
        return BaseResponse.onSuccess();
    }
}
