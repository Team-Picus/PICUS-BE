package com.picus.core.domain.chat.application.dto.request;

import com.picus.core.domain.chat.domain.entity.message.MessageType;
import com.picus.core.domain.reservation.entity.ReservationStatus;
import lombok.Builder;

@Builder
public record SendMsgReq (
        MessageType messageType,
        String content,         // Text Message
        Long imageId,           // Image Message
        Long reservationNo,     // Reservation Message
        ReservationStatus reservationStatus
) {}
