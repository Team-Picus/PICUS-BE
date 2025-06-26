package com.picus.core.old.domain.chat.application.dto.request;

import com.picus.core.old.domain.chat.domain.entity.message.MessageType;
import com.picus.core.old.domain.reservation.domain.entity.ReservationStatus;
import lombok.Builder;

@Builder
public record SendMsgReq (
        MessageType messageType,
        String content,         // Text Message
        Long imageId,           // Image Message
        String key,
        Long reservationNo,     // Reservation Message
        ReservationStatus reservationStatus
) {}
