package com.picus.core.reservation.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Reservation {

    private String reservationNo;
    private ReservationStatus reservationStatus;
    private String place;
    private LocalDateTime startTime;
    private String request;

    // 주문 도메인
}
