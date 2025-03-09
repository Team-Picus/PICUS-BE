package com.picus.core.domain.chat.domain.service.factory.strategy;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.application.dto.response.TextMessageRes;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.entity.message.TextMessage;
import com.picus.core.global.common.exception.RestApiException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._BAD_REQUEST;

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

        throw new RestApiException(_BAD_REQUEST);
    }
}