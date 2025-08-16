package com.picus.core.chat.adapter.out.persistence.mapper;


import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatParticipantEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class ChatRoomPersistenceMapperTest {

    private ChatRoomPersistenceMapper mapper = new ChatRoomPersistenceMapper();

    @Test
    @DisplayName("ChatRoomEntity -> ChatRoom 매핑")
    public void toDomain() throws Exception {
        // given
        ChatRoomEntity entity =
                createChatRoomEntity("cr-123");
        ChatParticipantEntity cp1 = createChatParticipantEntity("cp-1", "u-1", false, false);
        ChatParticipantEntity cp2 = createChatParticipantEntity("cp-2", "u-2", false, false);

        // when
        ChatRoom domain = mapper.toDomain(entity, List.of(cp1, cp2));

        // then
        assertThat(domain.getChatRoomNo()).isEqualTo(entity.getChatRoomNo());
        assertThat(domain.getChatParticipants()).hasSize(2)
                .extracting(
                        ChatParticipant::getChatParticipantNo,
                        ChatParticipant::getUserNo,
                        ChatParticipant::getIsPinned,
                        ChatParticipant::getIsExit
                ).containsExactlyInAnyOrder(
                        tuple(cp1.getChatParticipantNo(), cp1.getUserNo(), cp1.getIsPinned(), cp1.getIsExit()),
                        tuple(cp2.getChatParticipantNo(), cp2.getUserNo(), cp2.getIsPinned(), cp2.getIsExit())
                );
    }


    @Test
    @DisplayName("ChatRoom -> ChatRoomEntity 매핑")
    public void toChatRoomEntity() throws Exception {
        // given
        ChatRoom domain =
                createChatRoom("cr-123");

        // when
        ChatRoomEntity entity = mapper.toChatRoomEntity(domain);

        // then
        assertThat(entity.getChatRoomNo()).isEqualTo(domain.getChatRoomNo());
    }

    private ChatRoomEntity createChatRoomEntity(String chatRoomNo) {
        return ChatRoomEntity.builder()
                .chatRoomNo(chatRoomNo)
                .build();
    }

    private ChatParticipantEntity createChatParticipantEntity(String chatParticipantNo, String userNo, boolean isPinned, boolean isExit) {
        return ChatParticipantEntity.builder()
                .chatParticipantNo(chatParticipantNo)
                .userNo(userNo)
                .isPinned(isPinned)
                .isExit(isExit)
                .build();
    }

    private ChatRoom createChatRoom(String chatRoomNo) {
        return ChatRoom.builder()
                .chatRoomNo(chatRoomNo)
                .build();
    }

}