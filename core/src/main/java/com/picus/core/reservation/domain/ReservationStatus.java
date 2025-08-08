package com.picus.core.reservation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    REQUESTED("대기 중"),
    APPROVAL("요청 수락"),
    REJECTED("요청 거절"),
    IN_PROGRESS("활동 중"),
    ;

    private final String desc;
}
