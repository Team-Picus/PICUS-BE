package com.picus.core.reservation.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Refund {

    private String refundNo;
    private String reservationNo;

}
