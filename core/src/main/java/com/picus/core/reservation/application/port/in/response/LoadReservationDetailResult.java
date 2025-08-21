package com.picus.core.reservation.application.port.in.response;

import com.picus.core.reservation.domain.Reservation;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoadReservationDetailResult(
        Reservation reservation,
        String thumbnailImage
) {}
