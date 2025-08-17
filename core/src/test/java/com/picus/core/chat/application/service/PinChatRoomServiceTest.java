package com.picus.core.chat.application.service;

import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
import com.picus.core.chat.application.port.in.command.PinChatRoomCommand;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.application.port.out.ChatRoomUpdatePort;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.exception.RestApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class PinChatRoomServiceTest {

    @Mock
    ChatRoomReadPort chatRoomReadPort;
    @Mock
    ChatRoomUpdatePort chatRoomUpdatePort;

    @InjectMocks
    PinChatRoomService service;

    @Test
    @DisplayName("채팅방을 고정처리한다.")
    public void pin_success() throws Exception {
        // given
        String chatRoomNo = "cr1";
        String currentUserNo = "u-123";
        PinChatRoomCommand command = createCommand(List.of(chatRoomNo), currentUserNo);

        ChatParticipant me = createChatParticipant("cp-1", currentUserNo, false);
        ChatParticipant other = createChatParticipant("cp-1", "other", false);
        ChatRoom mockChatRoom = spy(createChatRoom(chatRoomNo, me, other));
        given(chatRoomReadPort.findAllByIds(command.chatRoomNos()))
                .willReturn(List.of(mockChatRoom));

        // when
        service.pin(command);

        // then
        then(chatRoomReadPort).should().findAllByIds(command.chatRoomNos());
        then(mockChatRoom).should().pin(me);
        then(chatRoomUpdatePort).should().bulkUpdateChatParticipant(List.of(me));
    }

    @Test
    @DisplayName("채팅방을 고정처리할 때 현재 사용자가 채팅방의 참여자가 아니면 에러 발생")
    public void pin_fail_currentUser_NotInChatRoom() throws Exception {
        // given
        String chatRoomNo = "cr1";
        String currentUserNo = "u-123";
        PinChatRoomCommand command = createCommand(List.of(chatRoomNo), currentUserNo);

        ChatParticipant other1 = createChatParticipant("cp-1", "other1", false);
        ChatParticipant other2 = createChatParticipant("cp-1", "other2", false);
        ChatRoom mockChatRoom = spy(createChatRoom(chatRoomNo, other1, other2)); // 현재 사용자가 없는 채팅방
        given(chatRoomReadPort.findAllByIds(command.chatRoomNos()))
                .willReturn(List.of(mockChatRoom));

        // when // then
        assertThatThrownBy(() -> service.pin(command))
                .isInstanceOf(RestApiException.class);
        then(chatRoomReadPort).should().findAllByIds(command.chatRoomNos());
    }

    private PinChatRoomCommand createCommand(List<String> chatRoomNoList, String currentUserNo) {
        return PinChatRoomCommand.builder()
                .chatRoomNos(chatRoomNoList)
                .currentUserNo(currentUserNo)
                .build();
    }
    private ChatRoom createChatRoom(String chatRoomNo, ChatParticipant cp1, ChatParticipant cp2) {
        return ChatRoom.builder()
                .chatRoomNo(chatRoomNo)
                .chatParticipants(List.of(cp1, cp2))
                .build();
    }
    private ChatParticipant createChatParticipant(String chatParticipantNo, String currentUserNo, boolean isPinned) {
        return ChatParticipant.builder()
                .chatParticipantNo(chatParticipantNo)
                .userNo(currentUserNo)
                .isExited(false)
                .isPinned(isPinned)
                .build();
    }
}