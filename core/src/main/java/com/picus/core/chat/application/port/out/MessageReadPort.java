package com.picus.core.chat.application.port.out;

import com.picus.core.chat.domain.model.Message;

import java.util.Optional;

public interface MessageReadPort {
    Optional<Message> findLastMessageByChatRoomNo(String chatRoomNo);

    long countUnReadMessageByChatRoomNoAndUserNo(String chatRoomNo, String userNo);
}
