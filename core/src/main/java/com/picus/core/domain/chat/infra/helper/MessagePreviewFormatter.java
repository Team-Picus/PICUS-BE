package com.picus.core.domain.chat.infra.helper;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.reservation.entity.ReservationStatus;
import org.springframework.stereotype.Component;

@Component
public class MessagePreviewFormatter {

    private final String IMAGE_PREVIEW = "사진을 보냈습니다.";

    public String preview(SendMsgReq request) {
        return switch (request.messageType()) {
            case TEXT -> request.content();
            case IMAGE -> IMAGE_PREVIEW;
            default -> convertToReservationMessage(request.reservationStatus(), request.reservationNo());
        };
    }

    private String convertToReservationMessage(ReservationStatus reservationStatus, Long reservationNo) {
        return switch (reservationStatus) {
            case CHECKING -> null; // todo
            default -> null;
        };
    }
}
