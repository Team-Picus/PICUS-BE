package com.picus.core.chat.adapter.out.persistence.jpa.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatParticipantEntityTest {

    @Test
    @DisplayName("ChatParticipant에 ChatRoomEntity을 바인딩한다.")
    public void bindChatRoomEntity() throws Exception {
        // given
        ChatParticipantEntity chatParticipantEntity = createChatParticipantEntity();
        ChatRoomEntity chatRoomEntity = createChatRoomEntity();

        // when
        chatParticipantEntity.bindChatRoomEntity(chatRoomEntity);

        // then
        Assertions.assertThat(chatParticipantEntity.getChatRoomEntity()).isEqualTo(chatRoomEntity);
    }

    private ChatRoomEntity createChatRoomEntity() {
        return ChatRoomEntity.builder().build();
    }
    private ChatParticipantEntity createChatParticipantEntity() {
        return ChatParticipantEntity.builder()
                .userNo("userNo")
                .isPinned(false)
                .isExit(false)
                .build();
    }
}