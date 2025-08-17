package com.picus.core.chat.application.port.out;

import com.picus.core.chat.domain.model.ChatParticipant;

import java.util.List;

public interface ChatRoomUpdatePort {

    void updateChatParticipant(ChatParticipant chatParticipant);

    void bulkUpdateChatParticipant(List<ChatParticipant> chatParticipants);
}
