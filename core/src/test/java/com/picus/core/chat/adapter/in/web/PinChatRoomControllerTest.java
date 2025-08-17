package com.picus.core.chat.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.chat.adapter.in.web.data.request.ExitChatRoomRequest;
import com.picus.core.chat.adapter.in.web.data.request.PinChatRoomRequest;
import com.picus.core.chat.adapter.in.web.mapper.ExitChatRoomWebMapper;
import com.picus.core.chat.adapter.in.web.mapper.PinChatRoomWebMapper;
import com.picus.core.chat.application.port.in.ExitChatRoomUseCase;
import com.picus.core.chat.application.port.in.PinChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
import com.picus.core.chat.application.port.in.command.PinChatRoomCommand;
import com.picus.core.shared.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PinChatRoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class PinChatRoomControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PinChatRoomUseCase useCase;
    @MockitoBean
    private PinChatRoomWebMapper webMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("채팅방 상단고정 요청 성공")
    public void pin_success() throws Exception {
        // given
        List<String> chatRoomNos = List.of("cr1", "cr2");
        PinChatRoomRequest request = PinChatRoomRequest.builder()
                .chatRoomNos(chatRoomNos)
                .build();
        String currentUserNo = TEST_USER_ID;

        PinChatRoomCommand mockCommand = mock(PinChatRoomCommand.class);
        given(webMapper.toCommand(request, currentUserNo)).willReturn(mockCommand);

        // when // then
        mockMvc.perform(
                        post("/api/v1/chats/pin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        then(webMapper).should().toCommand(request, currentUserNo);
        then(useCase).should().pin(mockCommand);
    }

    @Test
    @DisplayName("chatRoomNos 누락 오류")
    public void pin_fail_chatRoomNos_null() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        post("/api/v1/chats/pin")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        // then
    }

    @Test
    @DisplayName("chatRoomNos 길이 오류")
    public void pin_fail_chatRoomNos_size_error() throws Exception {
        // given
        List<String> chatRoomNos = List.of();
        PinChatRoomRequest request = PinChatRoomRequest.builder()
                .chatRoomNos(chatRoomNos)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/chats/pin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}