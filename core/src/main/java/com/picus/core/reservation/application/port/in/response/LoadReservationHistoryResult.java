package com.picus.core.reservation.application.port.in.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadReservationHistoryResult (
        String reservationNo,
        String thumbnailImage,
        String reservationStatus,
        String expertName,
        LocalDateTime dateTime,
        String place
) {}
