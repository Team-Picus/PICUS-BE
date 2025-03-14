package com.picus.core.domain.chat.domain.factory.strategy;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.domain.entity.message.ImageMessage;
import com.picus.core.domain.chat.domain.entity.message.Message;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageMessageCreationStrategy implements MessageCreationStrategy {

    private static final ImageMessageCreationStrategy INSTANCE = new ImageMessageCreationStrategy();

    public static ImageMessageCreationStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public Message toEntity(Long roomNo, Long senderNo, SendMsgReq request) {
        return new ImageMessage(
                roomNo,
                senderNo,
                request.imageId()
        );
    }

    @Override
    public MessageRes toDto(Message message, Integer unreadCnt) {
        return null;
    }
}
