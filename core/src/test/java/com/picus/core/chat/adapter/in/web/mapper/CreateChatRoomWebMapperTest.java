package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.request.CreateChatRoomRequest;
import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateChatRoomWebMapperTest {

    private CreateChatRoomWebMapper webMapper = new CreateChatRoomWebMapper();

    @Test
    @DisplayName("clientNo, CreateChatRoomRequest -> Command 매핑")
    public void toCommand() throws Exception {
        // given
        String clientNo = "c-123";
        CreateChatRoomRequest request = CreateChatRoomRequest.builder().expertNo("e-123").build();

        // when
        CreateChatRoomCommand command = webMapper.toCommand(clientNo, request);

        // then
        assertThat(command.clientNo()).isEqualTo(clientNo);
        assertThat(command.expertNo()).isEqualTo(request.expertNo());
    }

}