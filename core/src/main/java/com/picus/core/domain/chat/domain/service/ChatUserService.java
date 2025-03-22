package com.picus.core.domain.chat.domain.service;

import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.domain.chat.domain.entity.participant.ChatUserId;
import com.picus.core.domain.chat.domain.repository.ChatUserRepository;
import com.picus.core.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository chatUserRepository;

    public ChatUser findById(Long userNo, Long roomNo) {
        return chatUserRepository.findById(new ChatUserId(userNo, roomNo))
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    public ChatUser create(Long userNo, ChatRoom chatRoom) {
        ChatUser chatUser = chatUserRepository.save(new ChatUser(userNo, chatRoom.getId()));
        chatRoom.enterUser(chatUser.getUserNo());

        return chatUser;
    }
}
