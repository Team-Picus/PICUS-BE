package com.picus.core.old.domain.chat.infra.helper;

import com.picus.core.old.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.old.domain.reservation.domain.entity.ReservationStatus;
import com.picus.core.old.domain.chat.domain.entity.message.MessageType;
import org.springframework.stereotype.Component;

@Component
public class MessagePreviewFormatter {

    private final String IMAGE_PREVIEW = "사진을 보냈습니다.";

    public String preview(SendMsgReq request) {
        return switch (request.messageType()) {
            case MessageType.TEXT -> request.content();
            case MessageType.IMAGE -> IMAGE_PREVIEW;
            default -> convertToReservationMessage(request.reservationStatus(), request.reservationNo());
        };
    }

    // todo: 예약 플로우 정립 후 개발
    private String convertToReservationMessage(ReservationStatus reservationStatus, Long reservationNo) {
        return switch (reservationStatus) {
            case CHECKING -> null; // todo
            default -> null;
        };
    }
}
