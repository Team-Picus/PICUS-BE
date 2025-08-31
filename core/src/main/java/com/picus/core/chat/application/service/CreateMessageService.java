package com.picus.core.chat.application.service;

import com.picus.core.chat.application.port.in.CreateMessageUseCase;
import com.picus.core.chat.application.port.in.command.*;
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
import com.picus.core.chat.domain.model.vo.TextContent;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@RequiredArgsConstructor
@Transactional
public class CreateMessageService implements CreateMessageUseCase {

    private final ChatRoomReadPort chatRoomReadPort;
    private final MessageCreatePort messageCreatePort;

    private final CreateMessageCommandMapper commandMapper;

    @Override
    public CreateMessageResult create(CreateMessageCommand command) {

        // 채팅방 존재 여부 확인
        ChatRoom chatRoom = chatRoomReadPort.findById(command.chatRoomNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // 해당 사용자가 채팅방 참여자인지 확인
        List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();
        throwIfNotParticipant(command, chatParticipants);

        // 메시지 읽음여부 생성
        List<MessageReadStatus> messageReadStatuses = createReadStatuses(command, chatParticipants);

        switch (command) {
            case CreateTextMessageCommand textMessageCommand -> {

                // 채팅 메시지 생성 및 저장
                Message textMessage = commandMapper.toDomain(textMessageCommand, messageReadStatuses);
                messageCreatePort.create(textMessage);

                return CreateTextMessageResult.builder()
                        .senderNo(textMessage.getSenderNo())
                        .text(((TextContent) textMessage.getContent()).text())
                        .build();
            }
            case CreateFileMessageCommand createFileMessageCommand -> {

                // 채팅 메시지 생성 및 저장
                Message fileMessage = commandMapper.toDomain(createFileMessageCommand, messageReadStatuses);
                messageCreatePort.create(fileMessage);

                return CreateFileMessageResult.builder()
                        .senderNo(fileMessage.getSenderNo())
                        .fileUrl("") // TODO: file key -> url
                        .build();
            }
            case CreateImageMessageCommand createImageMessageCommand -> {
                Message imageMessage = commandMapper.toDomain(createImageMessageCommand, messageReadStatuses);
                messageCreatePort.create(imageMessage);

                return CreateImageMessageResult.builder()
                        .senderNo(imageMessage.getSenderNo())
                        .imageUrl("") // TODO: file key -> url
                        .build();
            }
            case CreateReservationMessageCommand createReservationMessageCommand -> {
                // TODO: 예약 메시지 구현
            }
            default -> throw new RestApiException(_BAD_REQUEST);
        }

        return null;
    }

    private void throwIfNotParticipant(CreateMessageCommand command, List<ChatParticipant> chatParticipants) {
        if (chatParticipants.stream()
                .noneMatch(chatParticipant -> chatParticipant.getUserNo().equals(command.senderNo())))
            throw new RestApiException(_FORBIDDEN);
    }

    private List<MessageReadStatus> createReadStatuses(CreateMessageCommand messageCommand, List<ChatParticipant> chatParticipants) {
        List<MessageReadStatus> messageReadStatuses = new ArrayList<>();
        for (ChatParticipant chatParticipant : chatParticipants) {
            MessageReadStatus messageReadStatus = MessageReadStatus.builder()
                    .chatRoomNo(messageCommand.chatRoomNo())
                    .userNo(chatParticipant.getUserNo())
                    .isRead(chatParticipant.getUserNo().equals(messageCommand.senderNo()))
                    .build();
            messageReadStatuses.add(messageReadStatus);
        }
        return messageReadStatuses;
    }
}
