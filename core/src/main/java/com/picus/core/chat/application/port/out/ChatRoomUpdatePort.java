package com.picus.core.chat.application.port.out;

import com.picus.core.chat.domain.model.ChatParticipant;

public interface ChatRoomUpdatePort {

    void updateChatParticipant(ChatParticipant chatParticipant);
}
