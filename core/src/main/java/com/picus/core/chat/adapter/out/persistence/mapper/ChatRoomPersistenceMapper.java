package com.picus.core.chat.adapter.out.persistence.mapper;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatParticipantEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatRoomPersistenceMapper {

    public ChatRoom toDomain(ChatRoomEntity chatRoomEntity, List<ChatParticipantEntity> chatParticipantEntities) {
        return ChatRoom.builder()
                .chatRoomNo(chatRoomEntity.getChatRoomNo())
                .chatParticipants(chatParticipantEntities.stream()
                        .map(this::toChatParticipant)
                        .toList())
                .build();
    }

    private ChatParticipant toChatParticipant(ChatParticipantEntity chatParticipantEntity) {
        return ChatParticipant.builder()
                .chatParticipantNo(chatParticipantEntity.getChatParticipantNo())
                .userNo(chatParticipantEntity.getUserNo())
                .isPinned(chatParticipantEntity.getIsPinned())
                .isExit(chatParticipantEntity.getIsExit())
                .build();
    }

    public ChatRoomEntity toChatRoomEntity(ChatRoom domain) {
        return ChatRoomEntity.builder()
                .chatRoomNo(domain.getChatRoomNo())
                .build();
    }

    public ChatParticipantEntity toChatParticipantEntity(ChatParticipant chatParticipant) {
        return ChatParticipantEntity.builder()
                .userNo(chatParticipant.getUserNo())
                .isPinned(chatParticipant.getIsPinned())
                .isExit(chatParticipant.getIsExit())
                .build();
    }

}
