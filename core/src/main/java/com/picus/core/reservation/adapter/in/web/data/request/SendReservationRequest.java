package com.picus.core.reservation.adapter.in.web.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import static com.picus.core.reservation.application.port.in.request.SaveReservationCommand.OptionSelection;

public record SendReservationRequest (
        @NotBlank String priceNo,
        @NotBlank String packageNo,
        List<OptionSelectionRequest> selectedOptions,
        @NotBlank String place,
        @NotNull LocalDateTime startTime,
        @NotBlank String requestDetail
) {
    public record OptionSelectionRequest(
            @NotBlank String optionNo,
            @NotNull Integer orderCnt
    ) {
        public OptionSelection toOptionSelection() {
            return new OptionSelection(optionNo, orderCnt);
        }
    }
}