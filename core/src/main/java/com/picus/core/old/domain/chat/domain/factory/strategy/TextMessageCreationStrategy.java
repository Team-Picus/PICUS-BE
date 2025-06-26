package com.picus.core.old.domain.chat.domain.factory.strategy;

import com.picus.core.old.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.old.domain.chat.application.dto.response.MessageRes;
import com.picus.core.old.domain.chat.application.dto.response.TextMessageRes;
import com.picus.core.old.domain.chat.domain.entity.message.Message;
import com.picus.core.old.domain.chat.domain.entity.message.TextMessage;
import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus;
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
    public Message toEntity(Long roomNo, Long senderNo, SendMsgReq request) {
        return new TextMessage(
                roomNo,
                senderNo,
                request.content()
        );
    }

    @Override
    public MessageRes toDto(Message message, Integer unreadCnt) {
        if(message instanceof TextMessage tm)
            return TextMessageRes.createRes(tm, unreadCnt);

        throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
    }
}