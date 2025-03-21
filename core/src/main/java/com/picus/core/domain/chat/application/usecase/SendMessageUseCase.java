package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.application.dto.response.SystemMessageRes;
import com.picus.core.domain.chat.application.dto.response.TextMessageRes;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.entity.message.SystemMessage;
import com.picus.core.domain.chat.domain.entity.message.TextMessage;
import com.picus.core.domain.chat.domain.service.ChatRoomService;
import com.picus.core.domain.chat.domain.service.MessageService;
import com.picus.core.domain.chat.domain.factory.MessageFactory;
import com.picus.core.domain.chat.infra.helper.MessagePreviewFormatter;
import com.picus.core.global.utils.StompHeaderAccessorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final MessageFactory messageFactory;
    private final ChatRoomService chatRoomService;
    private final MessagePreviewFormatter messagePreviewFormatter;
    private final MessageService messageService;
    private final OnlineStatusUseCase onlineStatusUseCase;
    private final RabbitTemplate rabbitTemplate;
    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;

    private static final Integer CHAT_USER_CNT = 2;     // 1대1 채팅
    private final String ROUTING_KEY_PREFIX = "room.";  // todo: transfer to MessageBroker

    public void sendMessage(StompHeaderAccessor accessor, SendMsgReq request) {
        Long userNo = stompHeaderAccessorUtil.getUserNoInSession(accessor);
        Long roomNo = stompHeaderAccessorUtil.getChatRoomNoInSession(accessor);

        if (chatRoomService.isExist(roomNo)) {
            Message message = messageFactory.toEntity(roomNo, userNo, request);   // 타입에 따라 생성
            messageService.save(message);
            chatRoomService.updateLastMessage(roomNo, messagePreviewFormatter.preview(request));

            int unreadCnt = calculateUnreadCnt(roomNo);
            publish(message, unreadCnt, roomNo);
        }
    }

    public void sendSystemMessage(Long roomNo, String content) {
        if(chatRoomService.isExist(roomNo)) {
            Message message = new SystemMessage(roomNo, content);
            messageService.save(message);
            chatRoomService.updateLastMessage(roomNo, content);    // todo: 적절한 워딩으로 변경 예정

            publishSystemMessage(message, roomNo);
        }
    }

    private void publish(Message message, int unreadCnt, Long roomNo) {
        MessageRes messageRes = messageFactory.toDto(message, unreadCnt); // todo: handle other type
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + roomNo, messageRes);
    }

    private void publishSystemMessage(Message message, Long roomNo) {
        MessageRes messageRes = SystemMessageRes.createRes((SystemMessage) message);
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + roomNo, messageRes);
    }
    private int calculateUnreadCnt(Long roomNo) {
        int onlineMemberCnt = onlineStatusUseCase.getOnlineMemberCntInChatRoom(roomNo);
        return CHAT_USER_CNT - onlineMemberCnt;
    }
}
