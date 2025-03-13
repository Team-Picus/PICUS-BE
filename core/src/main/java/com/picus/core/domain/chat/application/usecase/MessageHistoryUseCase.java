package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.domain.chat.domain.repository.MessageRepository;
import com.picus.core.domain.chat.domain.service.ChatUserService;
import com.picus.core.domain.chat.domain.factory.MessageFactory;
import com.picus.core.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MessageHistoryUseCase {

    private final MessageRepository messageRepository;
    private final OnlineStatusUseCase onlineStatusUseCase;
    private final ChatUserService chatUserService;
    private final MessageFactory messageFactory;

    private final Integer ROOM_SIZE = 2;

    public Message findById(Long messageNo) {
        return messageRepository.findById(messageNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public List<MessageRes> readMessages(Long roomNo, Long userNo, Optional<Long> last) {
        if (last.isEmpty()) {   // 채팅방 입장 시 요청
            List<MessageRes> newMessages = readNewMessage(roomNo, userNo);  // 과거 50개
            List<MessageRes> messages = readHistory(roomNo, newMessages.getFirst().getId());    // 안읽은 쌓인 메세지
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

    private List<MessageRes> readHistory(Long roomNo, Long lastMessageNo) {
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
