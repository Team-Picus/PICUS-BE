package com.picus.core.chat.application.port.out;

import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;

import java.util.List;

public interface ChatRoomCreatePort {
    ChatRoom create(List<ChatParticipant> participants);
}
