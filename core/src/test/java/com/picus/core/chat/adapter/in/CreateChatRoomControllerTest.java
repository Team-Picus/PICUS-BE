package com.picus.core.chat.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picus.core.chat.adapter.in.web.data.request.CreateChatRoomRequest;
import com.picus.core.chat.adapter.in.web.mapper.CreateChatRoomWebMapper;
import com.picus.core.chat.application.port.in.CreateChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;
import com.picus.core.infrastructure.security.AbstractSecurityMockSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CreateChatRoomController.class)
@AutoConfigureMockMvc(addFilters = false)
class CreateChatRoomControllerTest extends AbstractSecurityMockSetup {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateChatRoomUseCase useCase;
    @MockitoBean
    private CreateChatRoomWebMapper webMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("채팅방 생성 요청")
    public void create() throws Exception {
        // given
        String expertNo = "e-123";
        CreateChatRoomRequest request = CreateChatRoomRequest.builder().expertNo(expertNo).build();
        String clientNo = TEST_USER_ID;

        CreateChatRoomCommand mockCommand = mock(CreateChatRoomCommand.class);
        given(webMapper.toCommand(clientNo, request)).willReturn(mockCommand);
        given(useCase.create(mockCommand)).willReturn(anyString());

        // when // then
        mockMvc.perform(
                        post("/api/v1/chats")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
        // then
        then(webMapper).should().toCommand(clientNo, request);
        then(useCase).should().create(mockCommand);
    }


}