package com.picus.core.reservation.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadReservationHistoryResponse(
        String reservationNo,
        String thumbnailImage,
        String reservationStatus,
        String expertName,
        LocalDateTime dateTime,
        String place
) {}
