package com.picus.core.chat.application.port.out;

import com.picus.core.chat.domain.model.ChatRoom;

public interface ChatRoomCreatePort {
    ChatRoom create(ChatRoom chatRoom);
}
