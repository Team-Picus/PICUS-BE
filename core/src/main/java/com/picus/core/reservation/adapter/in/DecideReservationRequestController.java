package com.picus.core.reservation.adapter.in;

import com.picus.core.reservation.application.port.in.DecideReservationRequestUseCase;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideReservationRequestController {

    private final DecideReservationRequestUseCase decideReservationRequestUseCase;

    @PostMapping("/api/v1/reservation/request/{requestNo}/approval")
    public BaseResponse<Void> approval(
            @CurrentUser String expertNo,
            @PathVariable String requestNo
    ) {
        decideReservationRequestUseCase.approval(expertNo, requestNo);
        return BaseResponse.onSuccess();
    }

    @PostMapping("/api/v1/reservation/request/{requestNo}/reject")
    public BaseResponse<Void> reject(
            @CurrentUser String expertNo,
            @PathVariable String requestNo
    ) {
        decideReservationRequestUseCase.reject(expertNo, requestNo);
        return BaseResponse.onSuccess();
    }
}
