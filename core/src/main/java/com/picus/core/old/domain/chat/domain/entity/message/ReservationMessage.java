package com.picus.core.old.domain.chat.domain.entity.message;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("reservationMessage")
public class ReservationMessage extends Message {

    private Long reservationNo;

    public ReservationMessage(Long roomNo, Long senderNo, Long reservationNo) {
        super(roomNo, senderNo, MessageType.RESERVATION);
        this.reservationNo = reservationNo;
    }
}
