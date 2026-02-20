package com.picus.core.reservation.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Refund {

    private String refundNo;
    private String reservationNo;

}
