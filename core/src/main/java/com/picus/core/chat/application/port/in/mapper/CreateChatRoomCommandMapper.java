package com.picus.core.chat.application.port.in.mapper;

import com.picus.core.chat.domain.model.ChatParticipant;
import org.springframework.stereotype.Component;

@Component
public class CreateChatRoomCommandMapper {

    public ChatParticipant toChatParticipantDomain(String participantNo) {
        return ChatParticipant.builder()
                .userNo(participantNo)
                .isPinned(false)
                .isExited(false)
                .build();
    }
}
