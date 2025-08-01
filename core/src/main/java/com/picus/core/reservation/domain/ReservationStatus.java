package com.picus.core.reservation.domain;

public enum ReservationStatus {
    REQUESTED,    // 예약 요청 응답 대기중
    APPROVAL,   // 예약 요청 수락
    REJECTED,   // 예약 요청 거절

}
