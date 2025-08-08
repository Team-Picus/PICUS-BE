package com.picus.core.reservation.adapter.in.web.data.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record LoadReservationHistoryResponse(
        String reservationNo,
        String thumbnailImage,
        LocalDate date,
        String reservationStatus,
        String expertName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String place
) {}
