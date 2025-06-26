package com.picus.core.old.domain.chat.domain.factory.strategy;

import com.picus.core.old.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.old.domain.chat.application.dto.response.MessageRes;
import com.picus.core.old.domain.chat.domain.entity.message.Message;
import com.picus.core.old.domain.chat.domain.entity.message.ReservationMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationMessageCreationStrategy implements MessageCreationStrategy {

    private static final ReservationMessageCreationStrategy INSTANCE = new ReservationMessageCreationStrategy();

    public static ReservationMessageCreationStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Message toEntity(Long roomNo, Long senderNo, SendMsgReq request) {
        return new ReservationMessage(
                roomNo,
                senderNo,
                request.reservationNo()
        );
    }

    @Override
    public MessageRes toDto(Message message, Integer unreadCnt) {
        return null;
    }
}
