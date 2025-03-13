package com.picus.core.domain.chat.domain.factory;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.entity.message.MessageType;
import com.picus.core.domain.chat.domain.factory.strategy.ImageMessageCreationStrategy;
import com.picus.core.domain.chat.domain.factory.strategy.MessageCreationStrategy;
import com.picus.core.domain.chat.domain.factory.strategy.ReservationMessageCreationStrategy;
import com.picus.core.domain.chat.domain.factory.strategy.TextMessageCreationStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageFactory {

        private final Map<MessageType, MessageCreationStrategy> creationStrategies;

        private MessageFactory() {
            this.creationStrategies = Map.of(
                    MessageType.TEXT, TextMessageCreationStrategy.getInstance(),
                    MessageType.IMAGE, ImageMessageCreationStrategy.getInstance(),
                    MessageType.RESERVATION, ReservationMessageCreationStrategy.getInstance()
            );
        }

        public Message toEntity(Long roomNo, Long senderNo, SendMsgReq request) {
            return creationStrategies.get(request.messageType())
                    .toEntity(roomNo, senderNo, request);
        }

        public MessageRes toDto(Message message, Integer unreadCnt) {
            return creationStrategies.get(message.getMessageType())
                    .toDto(message, unreadCnt);
        }
}