package com.picus.core.reservation.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record LoadReservationDetailResponse(
        PostInfo postInfo,
        ReservationInfo reservationInfo,
        PackageInfo packageInfo,
        List<OptionInfo> optionInfos,
        PaymentInfo paymentInfo
) {
    @Builder
    public record PostInfo(
            String postName,
            String expertName,
            String postTheme,
            String thumbnailImage
    ) {}


    @Builder
    public record ReservationInfo(
            LocalDateTime dateTime,
            String place,
            List<String> moods,
            String request
    ) {}

    @Builder
    public record PackageInfo(
            String name,
            int price,
            List<String> contents,
            String notice
    ) {}

    @Builder
    public record OptionInfo(
            String name,
            int pricePerUnit,
            int unitSize,
            int orderCount,
            List<String> contents
    ) {}

    @Builder
    public record PaymentInfo(
//            int totalPrice,
//            String basePackage,
//            List<String> options
    ) {}
}
