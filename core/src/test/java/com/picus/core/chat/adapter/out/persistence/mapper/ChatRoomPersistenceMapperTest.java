package com.picus.core.chat.adapter.out.persistence.mapper;


import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.domain.model.ChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatRoomPersistenceMapperTest {

    private ChatRoomPersistenceMapper mapper = new ChatRoomPersistenceMapper();
    
    @Test
    @DisplayName("ChatRoomEntity -> ChatRoom 매핑")
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


    @Test
    @DisplayName("ChatRoom -> ChatRoomEntity 매핑")
    public void toEntity() throws Exception {
        // given
        ChatRoom domain =
                createChatRoom("cr-123", "c-123", "e-123", true);

        // when
        ChatRoomEntity entity = mapper.toEntity(domain);

        // then
        assertThat(entity.getChatRoomNo()).isEqualTo(domain.getChatRoomNo());
        assertThat(entity.getClientNo()).isEqualTo(domain.getClientNo());
        assertThat(entity.getExpertNo()).isEqualTo(domain.getExpertNo());
        assertThat(entity.getIsPinned()).isEqualTo(domain.getIsPinned());
    }

    private ChatRoomEntity createChatRoomEntity(String chatRoomNo, String clientNo, String expertNo, boolean isPinned) {
        return ChatRoomEntity.builder()
                .chatRoomNo(chatRoomNo)
                .clientNo(clientNo)
                .expertNo(expertNo)
                .isPinned(isPinned)
                .build();
    }

    private ChatRoom createChatRoom(String chatRoomNo, String clientNo, String expertNo, boolean isPinned) {
        return ChatRoom.builder()
                .chatRoomNo(chatRoomNo)
                .clientNo(clientNo)
                .expertNo(expertNo)
                .isPinned(isPinned)
                .build();
    }

}