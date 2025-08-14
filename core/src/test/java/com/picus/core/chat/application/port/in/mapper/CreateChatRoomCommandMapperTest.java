package com.picus.core.chat.application.port.in.mapper;

import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;
import com.picus.core.chat.domain.model.ChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateChatRoomCommandMapperTest {
    private final CreateChatRoomCommandMapper commandMapper = new CreateChatRoomCommandMapper();

    @Test
    @DisplayName("CreateChatRoomCommand -> ChatRoom 매핑")
    public void toDomain() throws Exception {
        // given
        CreateChatRoomCommand command = CreateChatRoomCommand.builder()
                .clientNo("c-123")
                .expertNo("e-123")
                .build();

        // when
        ChatRoom domain = commandMapper.toDomain(command);

        // then
        assertThat(domain.getClientNo()).isEqualTo("c-123");
        assertThat(domain.getExpertNo()).isEqualTo("e-123");
        assertThat(domain.getIsPinned()).isFalse();
    }
}