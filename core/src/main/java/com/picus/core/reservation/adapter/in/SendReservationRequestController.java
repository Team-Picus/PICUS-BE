package com.picus.core.reservation.adapter.in;

import com.picus.core.reservation.adapter.in.web.data.request.SendReservationRequest;
import com.picus.core.reservation.application.port.in.SendReservationRequestUseCase;
import com.picus.core.reservation.adapter.in.web.mapper.SendReservationWebMapper;
import com.picus.core.reservation.application.port.in.request.SaveReservationCommand;
import com.picus.core.shared.annotation.CurrentUser;
import com.picus.core.shared.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class SendReservationRequestController {

    private final SendReservationRequestUseCase sendReservationRequestUseCase;
    private final SendReservationWebMapper webMapper;

    @PostMapping("/request")
    public BaseResponse<Void> sendRequest(@CurrentUser String userNo, @RequestBody @Valid SendReservationRequest request) {
        SaveReservationCommand command = webMapper.toCommand(request);
        sendReservationRequestUseCase.send(userNo, command);
        return BaseResponse.onSuccess();
    }
}
