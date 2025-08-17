package com.picus.core.chat.adapter.in.web.mapper;


import com.picus.core.chat.adapter.in.web.data.request.ExitChatRoomRequest;
import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExitChatRoomWebMapperTest {

    private final ExitChatRoomWebMapper webMapper = new ExitChatRoomWebMapper();

    @Test
    @DisplayName("Request, currentUserNo ->  Command")
    public void toCommand() throws Exception {
        // given
        ExitChatRoomRequest request = ExitChatRoomRequest.builder()
                .chatRoomNos(List.of("cr1", "cr2"))
                .build();
        String currentUserNo = "user-123";

        // when
        ExitChatRoomCommand command = webMapper.toCommand(request, currentUserNo);

        // then
        assertThat(command.chatRoomNos()).hasSize(2)
                .containsExactlyInAnyOrder("cr1", "cr2");
        assertThat(command.currentUserNo()).isEqualTo(currentUserNo);
    }

}