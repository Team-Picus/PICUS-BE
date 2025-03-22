package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.application.dto.response.SyncRequestRes;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.domain.chat.domain.repository.MessageRepository;
import com.picus.core.domain.chat.domain.service.ChatRoomService;
import com.picus.core.domain.chat.domain.service.ChatUserService;
import com.picus.core.domain.user.domain.service.UserService;
import com.picus.core.global.utils.StompHeaderAccessorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SessionManagementUseCase {

    private final ChatRoomService chatRoomService;
    private final OnlineStatusUseCase onlineStatusUseCase;
    private final ChatUserService chatUserService;
    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;
    private final UserService userService;

    public void handleConnectMessage(StompHeaderAccessor accessor) {
        Long userNo = stompHeaderAccessorUtil.getUserNoInSession(accessor);
        Long roomNo = stompHeaderAccessorUtil.getChatRoomNoInSession(accessor);

        if (userService.isExist(userNo) && chatRoomService.isExist(roomNo)) {
            enterChatRoom(roomNo, userNo);
        }
    }

    public void handleDisconnectMessage(StompHeaderAccessor accessor) {
        Long userNo = stompHeaderAccessorUtil.removeMemberIdInSession(accessor);
        Long roomNo = stompHeaderAccessorUtil.removeChatRoomIdInSession(accessor);

        if (chatRoomService.isExist(roomNo)) {
            ChatUser chatUser = chatUserService.findById(userNo, roomNo);;
            chatUser.updateLastEntryTime();

            exitChatRoom(roomNo, userNo);
        }
    }

    private void enterChatRoom(Long chatRoomId, Long memberId) {
        onlineStatusUseCase.addChatRoom2Member(chatRoomId, memberId);
    }





    private void exitChatRoom(Long roomNo, Long userNo) {
        onlineStatusUseCase.removeChatRoom2Member(roomNo, userNo);
    }
}
