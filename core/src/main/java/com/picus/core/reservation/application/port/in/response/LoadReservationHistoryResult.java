package com.picus.core.reservation.application.port.in.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoadReservationHistoryResult (
        String reservationNo,
        String thumbnailImage,
//        LocalDate date,
        String reservationStatus,
        String expertName,
//        LocalDateTime startTime,
//        LocalDateTime endTime,
        String place
) {}
