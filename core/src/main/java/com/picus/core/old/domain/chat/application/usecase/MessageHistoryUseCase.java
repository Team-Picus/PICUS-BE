package com.picus.core.old.domain.chat.application.usecase;

import com.picus.core.old.domain.chat.application.dto.response.MessageRes;
import com.picus.core.old.domain.chat.application.dto.response.SyncRequestRes;
import com.picus.core.old.domain.chat.domain.entity.message.Message;
import com.picus.core.old.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.old.domain.chat.domain.repository.MessageRepository;
import com.picus.core.old.domain.chat.domain.service.ChatUserService;
import com.picus.core.old.domain.chat.domain.factory.MessageFactory;
import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageHistoryUseCase {

    private final MessageRepository messageRepository;
    private final OnlineStatusUseCase onlineStatusUseCase;
    private final ChatUserService chatUserService;
    private final MessageFactory messageFactory;
    private final RabbitTemplate rabbitTemplate;

    private final String ROUTING_KEY_PREFIX = "room.";
    private final Integer ROOM_SIZE = 2;

    public Message findById(String messageNo) {
        return messageRepository.findById(messageNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    @Transactional
    public List<MessageRes> readMessages(Long roomNo, Long userNo, Optional<String> last) {
        readUnreadMessages(roomNo, userNo);

        if (last.isEmpty()) {   // 채팅방 입장 시 요청
            List<MessageRes> newMessages = readNewMessage(roomNo, userNo);  // 새 메세지
            List<MessageRes> messages = new ArrayList<>(Optional.ofNullable(readHistory(roomNo, newMessages.getFirst().getId()))
                    .orElseGet(Collections::emptyList)); // 과거 50개 메세지

            messages.addAll(newMessages);

            return messages;
        }

        return readHistory(roomNo, last.get());     // 위로 스크롤 시 요청
    }

    private List<MessageRes> readNewMessage(Long roomNo, Long userNo) {
        ChatUser chatUser = chatUserService.findById(userNo, roomNo);
        List<Message> messages = messageRepository.findNewMessages(roomNo, chatUser.getLastEntryTime());

        return messages.stream()
                .map(message -> toDto(roomNo, message))
                .toList();
    }

    private void readUnreadMessages(Long roomNo, Long userNo) {
        ChatUser chatUser = chatUserService.findById(userNo, roomNo);
        LocalDateTime lastEntryTime = chatUser.getLastEntryTime();

        List<Message> unreadMessages = messageRepository.findUnreadMessages(roomNo, lastEntryTime);
        boolean existsOnlineChatRoomMember = onlineStatusUseCase.getOnlineMemberCntInChatRoom(roomNo) > 1; // 1은 본인

        if (!unreadMessages.isEmpty() && existsOnlineChatRoomMember) {
            unreadMessages.forEach(Message::updateIsRead);  // Update DB
            sendChatSyncRequestMessage(roomNo);     // Send Sync Msg
        }
    }

    private void sendChatSyncRequestMessage(Long chatRoomId) {
        MessageRes messageRes = SyncRequestRes.createRes();
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + chatRoomId, messageRes);
    }

    private List<MessageRes> readHistory(Long roomNo, String lastMessageNo) {
        Message lastMessage = findById(lastMessageNo);

        return messageRepository.findTop50ByRoomNoAndSendAtLessThanOrderBySendAtAsc(roomNo, lastMessage.getSendAt()).stream()
                .map(message -> toDto(roomNo, message))
                .toList();
    }

    private MessageRes toDto(Long roomNo, Message message) {
        int unreadCnt = ROOM_SIZE - onlineStatusUseCase.getOnlineMemberCntInChatRoom(roomNo);
        return messageFactory.toDto(message, unreadCnt);
    }
}
