package com.picus.core.chat.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.chat.adapter.in.web.data.request.ExitChatRoomRequest;
import com.picus.core.chat.adapter.in.web.mapper.ExitChatRoomWebMapper;
import com.picus.core.chat.application.port.in.ExitChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
import com.picus.core.shared.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ExitChatRoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExitChatRoomControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExitChatRoomUseCase useCase;
    @MockitoBean
    private ExitChatRoomWebMapper webMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("채팅방 나가기 요청 성공")
    public void exit_success() throws Exception {
        // given
        List<String> chatRoomNos = List.of("cr1", "cr2");
        ExitChatRoomRequest request = ExitChatRoomRequest.builder()
                .chatRoomNos(chatRoomNos)
                .build();
        String currentUserNo = TEST_USER_ID;

        ExitChatRoomCommand mockCommand = mock(ExitChatRoomCommand.class);
        given(webMapper.toCommand(request, currentUserNo)).willReturn(mockCommand);

        // when // then
        mockMvc.perform(
                        post("/api/v1/chats/exit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        then(webMapper).should().toCommand(request, currentUserNo);
        then(useCase).should().exit(mockCommand);
    }

    @Test
    @DisplayName("chatRoomNos 누락 오류")
    public void exit_fail_chatRoomNos_null() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                        post("/api/v1/chats/exit")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        // then
    }

    @Test
    @DisplayName("chatRoomNos 길이 오류")
    public void exit_fail_chatRoomNos_size_error() throws Exception {
        // given
        List<String> chatRoomNos = List.of();
        ExitChatRoomRequest request = ExitChatRoomRequest.builder()
                .chatRoomNos(chatRoomNos)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/chats/exit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}