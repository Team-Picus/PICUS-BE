package com.picus.core.chat.adapter.out.persistence.mapper;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.domain.model.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomPersistenceMapper {

    public ChatRoom toDomain(ChatRoomEntity entity) {
        return ChatRoom.builder()
                .chatRoomNo(entity.getChatRoomNo())
                .clientNo(entity.getClientNo())
                .expertNo(entity.getExpertNo())
                .isPinned(entity.getIsPinned())
                .build();
    }
}
