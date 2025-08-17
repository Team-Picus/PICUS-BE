package com.picus.core.chat.adapter.out.persistence.jpa.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


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
        assertThat(chatParticipantEntity.getChatRoomEntity()).isEqualTo(chatRoomEntity);
    }

    @Test
    @DisplayName("ChatParticipantEntity를 업데이트 한다.")
    public void update() throws Exception {
        // given
        ChatParticipantEntity chatParticipantEntity =
                createChatParticipantEntity(false, false);

        // when
        boolean newIsPinned = true;
        boolean newIsExited = true;
        LocalDateTime newExitedAt = LocalDateTime.of(2020, 1, 1, 1, 1);
        chatParticipantEntity.update(newIsPinned, newIsExited, newExitedAt);

        // then
        assertThat(chatParticipantEntity.getIsPinned()).isTrue();
        assertThat(chatParticipantEntity.getIsExited()).isTrue();
        assertThat(chatParticipantEntity.getExitedAt()).isEqualTo(newExitedAt);
    }

    private ChatRoomEntity createChatRoomEntity() {
        return ChatRoomEntity.builder().build();
    }
    private ChatParticipantEntity createChatParticipantEntity() {
        return ChatParticipantEntity.builder()
                .userNo("userNo")
                .isPinned(false)
                .isExited(false)
                .build();
    }
    private ChatParticipantEntity createChatParticipantEntity(boolean isPinned, boolean isExited) {
        return ChatParticipantEntity.builder()
                .userNo("userNo")
                .isPinned(isPinned)
                .isExited(isExited)
                .build();
    }
}