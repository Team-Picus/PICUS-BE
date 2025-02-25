package com.picus.core.domain.chat.domain.entity.message;

import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDateTime;

@TypeAlias("reservationMessage")
public class ReservationMessage extends Message {

    // 예약 플로우 컬럼

    public ReservationMessage(Long chattingRoomNo, Long senderId, LocalDateTime sendAt) {
        super(chattingRoomNo, senderId, sendAt, MessageType.RESERVATION, false);
    }
}
