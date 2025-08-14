package com.picus.core.chat.application.port.out;

import com.picus.core.chat.domain.model.ChatRoom;

import java.util.Optional;

public interface ChatRoomReadPort {
    Optional<ChatRoom> findByClientNoAndExpertNo(String clientNo, String expertNo);
}
