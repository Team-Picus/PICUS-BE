package com.picus.core.domain.chat.domain.service.factory.strategy;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.entity.message.TextMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TextMessageCreationStrategy implements MessageCreationStrategy {

    private static final TextMessageCreationStrategy INSTANCE = new TextMessageCreationStrategy();

    public static TextMessageCreationStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Message create(Long roomNo, Long senderNo, SendMsgReq request) {
        return new TextMessage(
                roomNo,
                senderNo,
                request.content()
        );
    }
}