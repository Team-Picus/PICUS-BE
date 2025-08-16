package com.picus.core.chat.application.port.in.mapper;

import com.picus.core.chat.domain.model.ChatParticipant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateChatRoomCommandMapperTest {
    private final CreateChatRoomCommandMapper commandMapper = new CreateChatRoomCommandMapper();

    @Test
    @DisplayName("participantNo -> ChatParticipant 매핑")
    public void toChatParticipantDomain() throws Exception {
        // given
        String participantNo = "u1";

        // when
        ChatParticipant domain = commandMapper.toChatParticipantDomain(participantNo);

        // then
        assertThat(domain.getUserNo()).isEqualTo(participantNo);
        assertThat(domain.getIsPinned()).isFalse();
        assertThat(domain.getIsExit()).isFalse();
    }
}