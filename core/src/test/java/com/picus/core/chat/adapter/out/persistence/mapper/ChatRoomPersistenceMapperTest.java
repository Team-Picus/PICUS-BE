package com.picus.core.chat.adapter.out.persistence.mapper;


import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.domain.model.ChatRoom;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatRoomPersistenceMapperTest {

    private ChatRoomPersistenceMapper mapper = new ChatRoomPersistenceMapper();
    
    @Test
    @DisplayName("ChatRoomEnity -> ChatRoom 매핑")
    public void toDomain() throws Exception {
        // given
        ChatRoomEntity entity =
                createChatRoomEntity("cr-123", "c-123", "e-123", true);

        // when
        ChatRoom domain = mapper.toDomain(entity);

        // then
        assertThat(domain.getChatRoomNo()).isEqualTo(entity.getChatRoomNo());
        assertThat(domain.getClientNo()).isEqualTo(entity.getClientNo());
        assertThat(domain.getExpertNo()).isEqualTo(entity.getExpertNo());
        assertThat(domain.getIsPinned()).isEqualTo(entity.getIsPinned());
    }

    private ChatRoomEntity createChatRoomEntity(String chatRoomNo, String clientNo, String expertNo, boolean isPinned) {
        return ChatRoomEntity.builder()
                .chatRoomNo(chatRoomNo)
                .clientNo(clientNo)
                .expertNo(expertNo)
                .isPinned(isPinned)
                .build();
    }

}