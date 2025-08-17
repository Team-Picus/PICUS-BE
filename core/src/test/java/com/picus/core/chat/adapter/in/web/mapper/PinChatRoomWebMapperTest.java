package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.request.PinChatRoomRequest;
import com.picus.core.chat.application.port.in.command.PinChatRoomCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
class PinChatRoomWebMapperTest {

    private final PinChatRoomWebMapper webMapper = new PinChatRoomWebMapper();

    @Test
    @DisplayName("Request, currentUserNo ->  Command")
    public void toCommand() throws Exception {
        // given
        PinChatRoomRequest request = PinChatRoomRequest.builder()
                .chatRoomNos(List.of("cr1", "cr2"))
                .build();
        String currentUserNo = "user-123";

        // when
        PinChatRoomCommand command = webMapper.toCommand(request, currentUserNo);

        // then
        assertThat(command.chatRoomNos()).hasSize(2)
                .containsExactlyInAnyOrder("cr1", "cr2");
        assertThat(command.currentUserNo()).isEqualTo(currentUserNo);
    }
}