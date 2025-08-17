package com.picus.core.chat.application.port.out;

import com.picus.core.chat.domain.model.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomReadPort {
    Optional<ChatRoom> findById(String chatRoomNo);

    List<ChatRoom> findAllByIds(List<String> chatRoomNos);

    Optional<ChatRoom> findByClientNoAndExpertNo(String clientNo, String expertNo);
}
