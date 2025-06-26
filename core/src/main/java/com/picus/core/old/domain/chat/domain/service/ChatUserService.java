package com.picus.core.old.domain.chat.domain.service;

import com.picus.core.old.domain.chat.domain.entity.ChatRoom;
import com.picus.core.old.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.old.domain.chat.domain.entity.participant.ChatUserId;
import com.picus.core.old.domain.chat.domain.repository.ChatUserRepository;
import com.picus.core.old.global.common.exception.RestApiException;
import com.picus.core.old.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository chatUserRepository;

    public ChatUser findById(Long userNo, Long roomNo) {
        return chatUserRepository.findById(new ChatUserId(userNo, roomNo))
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    public ChatUser create(Long userNo, ChatRoom chatRoom) {
        ChatUser chatUser = chatUserRepository.save(new ChatUser(userNo, chatRoom.getId()));
        chatRoom.enterUser(chatUser.getUserNo());

        return chatUser;
    }
}
