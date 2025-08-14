package com.picus.core.chat.application.service;


import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;
import com.picus.core.chat.application.port.in.mapper.CreateChatRoomCommandMapper;
import com.picus.core.chat.application.port.out.ChatRoomCreatePort;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.picus.core.user.domain.model.Role.CLIENT;
import static com.picus.core.user.domain.model.Role.EXPERT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CreateChatRoomServiceTest {

    @Mock
    private UserReadPort userReadPort;

    @Mock
    private ChatRoomCreatePort chatRoomCreatePort;
    @Mock
    private ChatRoomReadPort chatRoomReadPort;

    @Mock
    CreateChatRoomCommandMapper commandMapper;

    @InjectMocks
    CreateChatRoomService service;

    @Test
    @DisplayName("채팅방을 생성할 때 해당 클라이언트-전문가 조합으로 채팅방이 존재하면 기존 채팅방 No를 반환한다.")
    public void create_ifChatRoomExist_returnExistChatRoomNo() throws Exception {
        // given
        CreateChatRoomCommand command = CreateChatRoomCommand.builder()
                .clientNo("c-123")
                .expertNo("e-123")
                .build();

        given(userReadPort.findRoleById(command.expertNo())).willReturn(EXPERT);
        String mockChatRoomNo = "cr-123";
        ChatRoom mockChatRoom = createMockChatRoom(mockChatRoomNo);
        given(chatRoomReadPort.findByClientNoAndExpertNo(command.clientNo(), command.expertNo()))
                .willReturn(Optional.ofNullable(mockChatRoom));

        // when
        String returnChatRoomNo = service.create(command);

        // then
        then(userReadPort).should().findRoleById(command.expertNo());
        then(chatRoomReadPort).should().findByClientNoAndExpertNo(command.clientNo(), command.expertNo());

        assertThat(returnChatRoomNo).isEqualTo(mockChatRoomNo);
    }

    @Test
    @DisplayName("채팅방을 생성할 때 해당 클라이언트-전문가 조합으로 채팅방이 존재하지 않으면 새로운 채팅방을 만들고, " +
            "해당 chatRoomNo를 반환한다.")
    public void create_ifChatRoomNotExist_returnNewChatRoomNo() throws Exception {
        // given
        CreateChatRoomCommand command = CreateChatRoomCommand.builder()
                .clientNo("c-123")
                .expertNo("e-123")
                .build();

        given(userReadPort.findRoleById(command.expertNo())).willReturn(EXPERT);
        given(chatRoomReadPort.findByClientNoAndExpertNo(command.clientNo(), command.expertNo()))
                .willReturn(Optional.empty());

        ChatRoom mockNewChatRoom = createMockChatRoom(null); // 새로 생성되기 전에는 chatRoomNo가 없음
        given(commandMapper.toDomain(command)).willReturn(mockNewChatRoom);

        String createdChatRoomNo = "cr-123";
        ChatRoom mockCreatedChatRoom = createMockChatRoom(createdChatRoomNo); // 데이터베이스에 저장되면 ChatRoomNo가 생김ㄴ
        given(chatRoomCreatePort.create(mockNewChatRoom)).willReturn(mockCreatedChatRoom);

        // when
        String returnChatRoomNo = service.create(command);

        // then
        then(userReadPort).should().findRoleById(command.expertNo());
        then(chatRoomReadPort).should().findByClientNoAndExpertNo(command.clientNo(), command.expertNo());
        then(commandMapper).should().toDomain(command);
        then(chatRoomCreatePort).should().create(mockNewChatRoom);

        assertThat(returnChatRoomNo).isEqualTo(createdChatRoomNo);
    }

    @Test
    @DisplayName("채팅방을 생성할 때 채팅 상대방이 Expert가 아니면 오류가 발생한다.")
    public void create_ifPeerIsNotExpert_error() throws Exception {
        // given
        CreateChatRoomCommand command = CreateChatRoomCommand.builder()
                .clientNo("c-123")
                .expertNo("e-123")
                .build();

        given(userReadPort.findRoleById(command.expertNo())).willReturn(CLIENT);

        // when then
        assertThatThrownBy(() -> service.create(command)).isInstanceOf(RestApiException.class);

        // then
        then(userReadPort).should().findRoleById(command.expertNo());
    }

    private ChatRoom createMockChatRoom(String chatRoomNo) {
        return ChatRoom.builder()
                .chatRoomNo(chatRoomNo)
                .build();
    }


}