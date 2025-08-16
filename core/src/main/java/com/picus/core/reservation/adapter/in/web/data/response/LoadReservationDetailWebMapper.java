package com.picus.core.reservation.adapter.in.web.data.response;

import com.picus.core.reservation.application.port.in.response.LoadReservationDetailResult;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.SelectedOption;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.picus.core.reservation.adapter.in.web.data.response.LoadReservationDetailResponse.*;

@Component
public class LoadReservationDetailWebMapper {

    public LoadReservationDetailResponse toResponse(LoadReservationDetailResult result) {
        return builder()
                .postInfo(
                        PostInfo.builder()
                                .postName(result.reservation().getSelectedPost().getExpertName())
                                .expertName(result.reservation().getSelectedPost().getExpertName())
                                .postTheme(result.reservation().getSelectedPost().getTheme())
                                .thumbnailImage(result.thumbnailImage())
                                .build()
                )
                .reservationInfo(
                        ReservationInfo.builder()
                                .dateTime(result.reservation().getStartTime())
                                .place(result.reservation().getPlace())
                                .moods(result.reservation().getSelectedPost().getMoods())
                                .request(result.reservation().getRequestDetail())
                                .build()
                )
                .packageInfo(
                        PackageInfo.builder()
                                .name(result.reservation().getSelectedPackage().getName())
                                .price(result.reservation().getSelectedPackage().getPrice())
                                .contents(result.reservation().getSelectedPackage().getContents())
                                .notice(result.reservation().getSelectedPackage().getNotice())
                                .build()
                )
                .optionInfos(
                        mapOptions(result.reservation().getSelectedOptions())
                )
                .paymentInfo(
                        PaymentInfo.builder()
                                // todo: 요구사항 결정된 후 개발
                                .build()
                )
                .build();
    }

    private List<OptionInfo> mapOptions(List<SelectedOption> selectedOptions) {
        if (selectedOptions == null || selectedOptions.isEmpty()) {
            return Collections.emptyList();
        }
        return selectedOptions.stream()
                .filter(Objects::nonNull)
                .map(this::toOptionInfo)
                .toList();
    }

    private OptionInfo toOptionInfo(SelectedOption selectedOption) {
        return OptionInfo.builder()
                .name(selectedOption.getName())
                .pricePerUnit(selectedOption.getPricePerUnit())
                .unitSize(selectedOption.getUnitSize())
                .orderCount(selectedOption.getOrderCount())
                .contents(selectedOption.getContents())
                .build();
    }
}
