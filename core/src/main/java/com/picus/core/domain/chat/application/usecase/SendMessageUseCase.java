package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.service.ChatRoomService;
import com.picus.core.domain.chat.domain.service.factory.MessageFactory;
import com.picus.core.domain.chat.infra.client.internal.broker.MessageBrokerAdapter;
import com.picus.core.domain.chat.infra.helper.MessagePreviewFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.picus.core.global.config.RabbitMQConfig.CHAT_EXCHANGE_NAME;

@Service
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final MessageBrokerAdapter messageBrokerAdapter;
    private final MessageFactory messageFactory;
    private final ChatRoomService chatRoomService;
    private final MessagePreviewFormatter messagePreviewFormatter;

    @Transactional
    public void sendMessage(Long roomNo, Long senderId, SendMsgReq request) {
        if(chatRoomService.isExist(roomNo)) {
            Message message = messageFactory.create(roomNo, senderId, request);
            publishToMessageBroker(roomNo, message);
            chatRoomService.updateLastMessage(roomNo, messagePreviewFormatter.preview(request));
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    private void publishToMessageBroker(Long roomNo, Message message) {
        messageBrokerAdapter.convertAndSend(CHAT_EXCHANGE_NAME, "chat.room." + roomNo, message);
    }
}
