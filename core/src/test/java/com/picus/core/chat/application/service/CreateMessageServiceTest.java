package com.picus.core.chat.application.service;


import com.picus.core.chat.application.port.in.command.CreateFileMessageCommand;
import com.picus.core.chat.application.port.in.command.CreateImageMessageCommand;
import com.picus.core.chat.application.port.in.command.CreateTextMessageCommand;
import com.picus.core.chat.application.port.in.mapper.CreateMessageCommandMapper;
import com.picus.core.chat.application.port.in.result.CreateFileMessageResult;
import com.picus.core.chat.application.port.in.result.CreateImageMessageResult;
import com.picus.core.chat.application.port.in.result.CreateMessageResult;
import com.picus.core.chat.application.port.in.result.CreateTextMessageResult;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.application.port.out.MessageCreatePort;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.chat.domain.model.MessageReadStatus;
import com.picus.core.chat.domain.model.vo.FileContent;
import com.picus.core.chat.domain.model.vo.ImageContent;
import com.picus.core.chat.domain.model.vo.TextContent;
import com.picus.core.shared.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMessageServiceTest {

    @Mock
    private ChatRoomReadPort chatRoomReadPort;
    @Mock
    private MessageCreatePort messageCreatePort;
    @Spy
    private CreateMessageCommandMapper commandMapper = new CreateMessageCommandMapper();

    @InjectMocks
    private CreateMessageService service;

    @Test
    @DisplayName("채팅 메시지 저장1 - 텍스트 메시지를 저장하고 읽음여부를 생성한다.")
    public void create_text_message() throws Exception {
        // given
        CreateTextMessageCommand messageCommand = CreateTextMessageCommand.builder()
                .chatRoomNo("cr-123")
                .senderNo("u-123")
                .textContent(TextContent.builder().text("message").build())
                .build();

        ChatRoom mockChatRoom = mock(ChatRoom.class);
        given(chatRoomReadPort.findById(messageCommand.chatRoomNo()))
                .willReturn(Optional.of(mockChatRoom));

        ChatParticipant mockChatParticipant1 = mock(ChatParticipant.class); // 발신자
        ChatParticipant mockChatParticipant2 = mock(ChatParticipant.class); // 상대방
        given(mockChatRoom.getChatParticipants()).willReturn(List.of(mockChatParticipant1, mockChatParticipant2));
        given(mockChatParticipant1.getUserNo()).willReturn("u-123"); // sender
        given(mockChatParticipant2.getUserNo()).willReturn("u-999"); // other

        // when
        CreateMessageResult createMessageResult = service.create(messageCommand);

        // then - 메서드 호출 검증
        ArgumentCaptor<Message> captor = assertMethodCall(messageCommand.chatRoomNo());

        // then - create()파라미터 검증
        Message capturedMessage = captor.getValue();
        assertThat(capturedMessage.getChatRoomNo()).isEqualTo("cr-123");
        assertThat(capturedMessage.getSenderNo()).isEqualTo("u-123");
        assertThat(((TextContent) capturedMessage.getContent()).text()).isEqualTo("message");

        // then - getMessageReadStatuses 검증
        assertReadStatus(capturedMessage.getMessageReadStatuses());

        // then - 리턴값 검증
        assertThat(createMessageResult).isInstanceOf(CreateTextMessageResult.class);
        CreateTextMessageResult textMessageResult = (CreateTextMessageResult) createMessageResult;
        assertThat(textMessageResult.senderNo()).isEqualTo("u-123");
        assertThat(textMessageResult.text()).isEqualTo("message");
    }

    @Test
    @DisplayName("채팅 메시지 저장2 - 파일 메시지를 저장하고 읽음여부를 생성한다.")
    public void create_file_message() throws Exception {
        // given
        CreateFileMessageCommand messageCommand = CreateFileMessageCommand.builder()
                .chatRoomNo("cr-123")
                .senderNo("u-123")
                .fileContent(FileContent.builder().fileKey("a.pdf").build())
                .build();

        ChatRoom mockChatRoom = mock(ChatRoom.class);
        given(chatRoomReadPort.findById(messageCommand.chatRoomNo()))
                .willReturn(Optional.of(mockChatRoom));

        ChatParticipant mockChatParticipant1 = mock(ChatParticipant.class); // 발신자
        ChatParticipant mockChatParticipant2 = mock(ChatParticipant.class); // 상대방
        given(mockChatRoom.getChatParticipants()).willReturn(List.of(mockChatParticipant1, mockChatParticipant2));
        given(mockChatParticipant1.getUserNo()).willReturn("u-123"); // sender
        given(mockChatParticipant2.getUserNo()).willReturn("u-999"); // other

        // when
        CreateMessageResult messageResult = service.create(messageCommand);

        // then - 메서드 호출 검증
        ArgumentCaptor<Message> captor = assertMethodCall(messageCommand.chatRoomNo());

        // then - create() 파라미터 검증
        Message capturedMessage = captor.getValue();
        assertThat(capturedMessage.getChatRoomNo()).isEqualTo("cr-123");
        assertThat(capturedMessage.getSenderNo()).isEqualTo("u-123");
        assertThat(((FileContent) capturedMessage.getContent()).fileKey()).isEqualTo("a.pdf");

        // then - getMessageReadStatuses 검증
        assertReadStatus(capturedMessage.getMessageReadStatuses());

        // then - 리턴값 검증
        assertThat(messageResult).isInstanceOf(CreateFileMessageResult.class);
        CreateFileMessageResult fileMessageResult = (CreateFileMessageResult) messageResult;
        assertThat(fileMessageResult.senderNo()).isEqualTo("u-123");
        assertThat(fileMessageResult.fileUrl()).isEqualTo(""); // TODO: file key -> url

    }

    @Test
    @DisplayName("채팅 메시지 저장3 - 이미지 메시지를 저장하고 읽음여부를 생성한다.")
    public void create_image_message() throws Exception {
        // given
        CreateImageMessageCommand messageCommand = CreateImageMessageCommand.builder()
                .chatRoomNo("cr-123")
                .senderNo("u-123")
                .imageContent(ImageContent.builder().imageFileKey("a.png").build())
                .build();

        ChatRoom mockChatRoom = mock(ChatRoom.class);
        given(chatRoomReadPort.findById(messageCommand.chatRoomNo()))
                .willReturn(Optional.of(mockChatRoom));

        ChatParticipant mockChatParticipant1 = mock(ChatParticipant.class); // 발신자
        ChatParticipant mockChatParticipant2 = mock(ChatParticipant.class); // 상대방
        given(mockChatRoom.getChatParticipants()).willReturn(List.of(mockChatParticipant1, mockChatParticipant2));
        given(mockChatParticipant1.getUserNo()).willReturn("u-123"); // sender
        given(mockChatParticipant2.getUserNo()).willReturn("u-999"); // other

        // when
        CreateMessageResult messageResult = service.create(messageCommand);

        // then - 메서드 호출 검증
        ArgumentCaptor<Message> captor = assertMethodCall(messageCommand.chatRoomNo());

        // then - create() 파라미터 검증
        Message capturedMessage = captor.getValue();
        assertThat(capturedMessage.getChatRoomNo()).isEqualTo("cr-123");
        assertThat(capturedMessage.getSenderNo()).isEqualTo("u-123");
        assertThat(((ImageContent) capturedMessage.getContent()).imageFileKey()).isEqualTo("a.png");

        // then - getMessageReadStatuses 검증
        assertReadStatus(capturedMessage.getMessageReadStatuses());

        // then - 리턴값 검증
        assertThat(messageResult).isInstanceOf(CreateImageMessageResult.class);
        CreateImageMessageResult imageMessageResult = (CreateImageMessageResult) messageResult;
        assertThat(imageMessageResult.senderNo()).isEqualTo("u-123");
        assertThat(imageMessageResult.imageUrl()).isEqualTo(""); // TODO: file key -> url

    }

    @Test
    @DisplayName("채팅 메시지 저장 실패 - 채팅방 존재 X")
    public void create_fail_ifNotChatRoom_NotExist() throws Exception {
        // given
        CreateImageMessageCommand messageCommand = CreateImageMessageCommand.builder()
                .chatRoomNo("cr-123")
                .senderNo("u-123")
                .imageContent(ImageContent.builder().imageFileKey("a.png").build())
                .build();

        given(chatRoomReadPort.findById(messageCommand.chatRoomNo()))
                .willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> service.create(messageCommand))
                .isInstanceOf(RestApiException.class);
    }

    private ArgumentCaptor<Message> assertMethodCall(String messageCommand) {
        then(chatRoomReadPort).should().findById(messageCommand);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        then(messageCreatePort).should().create(captor.capture());
        return captor;
    }

    private void assertReadStatus(List<MessageReadStatus> statuses) {
        assertThat(statuses).hasSize(2);
        // 각 참여자에 대해 (userNo, isRead) 매칭 확인: 발신자만 읽음 true
        assertThat(statuses)
                .extracting(MessageReadStatus::getUserNo, MessageReadStatus::getIsRead)
                .containsExactlyInAnyOrder(
                        tuple("u-123", true),
                        tuple("u-999", false)
                );
        // 모든 읽음 상태에 chatRoomNo가 올바르게 세팅되었는지 확인
        assertThat(statuses).allSatisfy(s ->
                assertThat(s.getChatRoomNo()).isEqualTo("cr-123")
        );
    }
}