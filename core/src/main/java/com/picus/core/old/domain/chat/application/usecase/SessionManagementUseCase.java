package com.picus.core.old.domain.chat.application.usecase;

import com.picus.core.old.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.old.domain.chat.domain.service.ChatRoomService;
import com.picus.core.old.domain.chat.domain.service.ChatUserService;
import com.picus.core.old.domain.user.domain.service.UserService;
import com.picus.core.old.global.utils.StompHeaderAccessorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
