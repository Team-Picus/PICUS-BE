package com.picus.core.reservation.adapter.in.web.mapper;

import com.picus.core.reservation.adapter.in.web.data.request.SendReservationRequest;
import com.picus.core.reservation.application.port.in.request.SaveReservationCommand;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.picus.core.reservation.application.port.in.request.SaveReservationCommand.*;

@Component
public class SendReservationWebMapper {

    public SaveReservationCommand toCommand(SendReservationRequest request) {
        List<OptionSelection> selections = request.selectedOptions().stream()
                .map(SendReservationRequest.OptionSelectionRequest::toOptionSelection)
                .map(os -> new OptionSelection(os.optionNo(), os.count()))
                .toList();

        return builder()
                .postNo(request.postNo())
                .priceNo(request.priceNo())
                .packageNo(request.packageNo())
                .optionSelection(selections)
                .place(request.place())
                .startTime(request.startTime())
                .requestDetail(request.requestDetail())
                .build();
    }
}
