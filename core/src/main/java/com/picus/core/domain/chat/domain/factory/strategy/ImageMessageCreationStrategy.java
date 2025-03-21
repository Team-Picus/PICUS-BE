package com.picus.core.domain.chat.domain.factory.strategy;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.dto.response.ImageMessageRes;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.application.dto.response.TextMessageRes;
import com.picus.core.domain.chat.domain.entity.message.ImageMessage;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.entity.message.TextMessage;
import com.picus.core.global.common.exception.RestApiException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._BAD_REQUEST;

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
        if(message instanceof ImageMessage im)
            return ImageMessageRes.createRes(im, unreadCnt);

        throw new RestApiException(_BAD_REQUEST);
    }
}
