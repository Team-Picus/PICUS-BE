package com.picus.core.chat.application.service;

import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
import com.picus.core.chat.application.port.out.ChatRoomDeletePort;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.application.port.out.ChatRoomUpdatePort;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.exception.RestApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;


@ExtendWith(MockitoExtension.class)
class ExitChatRoomServiceTest {

    @Mock
    private ChatRoomReadPort chatRoomReadPort;
    @Mock
    private ChatRoomUpdatePort chatRoomUpdatePort;
    @Mock
    private ChatRoomDeletePort chatRoomDeletePort;

    @InjectMocks
    ExitChatRoomService service;

    @Test
    @DisplayName("채팅방을 나간다. 상대방이 채팅방에 나가 있으면 채팅방이 삭제된다.")
    public void exit_delete_room() throws Exception {
        // given - 파라미터 셋팅
        String chatRoomNo = "cr-1";
        List<String> chatRoomNoList = List.of(chatRoomNo);
        String currentUserNo = "user-123";

        ExitChatRoomCommand command = createCommand(chatRoomNoList, currentUserNo);

        // given - 메서드 Stubbing
        boolean otherIsExited = true; // 상대방이 현재 채팅방에 나간 상태
        ChatParticipant me = createChatParticipant("cp-1", currentUserNo, false);
        ChatParticipant other = createChatParticipant("cp-2", chatRoomNo + "_other", otherIsExited);
        stubbing(chatRoomNo, me, other);


        // when
        service.exit(command);

        // then
        then(chatRoomReadPort).should().findById(chatRoomNo);
        then(chatRoomDeletePort).should().delete(chatRoomNo);

        then(chatRoomUpdatePort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("채팅방을 나간다. 상대방이 채팅방에 들어와 있으면 현재 사용자만 나가고 채팅방이 유지된다.")
    public void exit_currentUser_exitOnly() throws Exception {
        // given - 파라미터 셋팅
        String chatRoomNo = "cr-1";
        List<String> chatRoomNoList = List.of(chatRoomNo);
        String currentUserNo = "user-123";

        ExitChatRoomCommand command = createCommand(chatRoomNoList, currentUserNo);

        // given - 메서드 Stubbing
        boolean otherIsExited = false; // 상대방이 아직 채팅방에 남아 있는 상태
        ChatParticipant spyMe = spy(createChatParticipant("cp-1", currentUserNo, false));
        ChatParticipant other = createChatParticipant("cp-2", chatRoomNo + "_other", otherIsExited);
        stubbing(chatRoomNo, spyMe, other);

        // when
        service.exit(command);

        // then
        then(chatRoomReadPort).should().findById(chatRoomNo);
        then(spyMe).should().exit(any(LocalDateTime.class));
        then(chatRoomUpdatePort).should().updateChatParticipant(spyMe);

        then(chatRoomDeletePort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("채팅방을 나갈 때 채팅방이 존재하지 않으면 에러가 발생한다.")
    public void exit_ifChatRoomNotExist() throws Exception {
        // given - 파라미터 셋팅
        String chatRoomNo = "cr-1";
        List<String> chatRoomNoList = List.of(chatRoomNo);
        String currentUserNo = "user-123";

        ExitChatRoomCommand command = createCommand(chatRoomNoList, currentUserNo);

        // given - 메서드 Stubbing
        given(chatRoomReadPort.findById(chatRoomNo)).willReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> service.exit(command))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("채팅방을 나갈 때 내가 속한 채팅방이 아니면 에러가 발생한다.")
    public void exit_ifNotMyChatRoom() throws Exception {
        // given - 파라미터 셋팅
        String chatRoomNo = "cr-1";
        List<String> chatRoomNoList = List.of(chatRoomNo);
        String currentUserNo = "user-123";

        ExitChatRoomCommand command = createCommand(chatRoomNoList, currentUserNo);

        // given - 메서드 Stubbing
        ChatParticipant other1 = createChatParticipant("cp-1", chatRoomNo + "_other1", false);
        ChatParticipant other2 = createChatParticipant("cp-2", chatRoomNo + "_other2", false);
        stubbing(chatRoomNo, other1, other2);

        // when // then
        assertThatThrownBy(() -> service.exit(command))
                .isInstanceOf(RestApiException.class);
    }

    private void stubbing(String chatRoomNo, ChatParticipant me, ChatParticipant other) {
        ChatRoom mockChatRoom = ChatRoom.builder()
                .chatRoomNo(chatRoomNo)
                .chatParticipants(List.of(me, other))
                .build();
        given(chatRoomReadPort.findById(chatRoomNo)).willReturn(Optional.ofNullable(mockChatRoom));
    }


    private ChatParticipant createChatParticipant(String chatParticipantNo, String currentUserNo, boolean isExited) {
        return ChatParticipant.builder()
                .chatParticipantNo(chatParticipantNo)
                .userNo(currentUserNo)
                .isExited(isExited)
                .isPinned(false)
                .build();
    }

    private ExitChatRoomCommand createCommand(List<String> chatRoomNoList, String currentUserNo) {
        return ExitChatRoomCommand.builder()
                .chatRoomNos(chatRoomNoList)
                .currentUserNo(currentUserNo)
                .build();
    }

}