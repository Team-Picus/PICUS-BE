package com.picus.core.reservation.application.port.in.request;

import com.picus.core.shared.common.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class SaveReservationCommand extends SelfValidating<SaveReservationCommand> {
    @NotBlank
    String priceNo;

    @NotBlank
    String packageNo;

    List<OptionSelection> optionSelection;

    @NotBlank
    String postNo;

    @NotBlank
    String place;

    @NotNull
    LocalDateTime startTime;

    @NotBlank
    String requestDetail;

    public SaveReservationCommand(String priceNo, String packageNo, List<OptionSelection> optionSelection, String place, LocalDateTime startTime, String requestDetail) {
        this.priceNo = priceNo;
        this.packageNo = packageNo;
        this.optionSelection = optionSelection;
        this.place = place;
        this.startTime = startTime;
        this.requestDetail = requestDetail;
        this.validateSelf();
    }

    public record OptionSelection(
            String optionNo,
            Integer count
    ) {}
}
