package com.picus.core.chat.application.service;

import com.picus.core.chat.application.port.in.CreateChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.CreateChatRoomCommand;
import com.picus.core.chat.application.port.in.mapper.CreateChatRoomCommandMapper;
import com.picus.core.chat.application.port.out.ChatRoomCreatePort;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class CreateChatRoomService implements CreateChatRoomUseCase {

    private final UserReadPort userReadPort;

    private final ChatRoomCreatePort chatRoomCreatePort;
    private final ChatRoomReadPort chatRoomReadPort;

    private final CreateChatRoomCommandMapper commandMapper;

    @Override
    public String create(CreateChatRoomCommand command) {

        // 채팅 상대방이 Expert인지 확인
        Role peerRole = userReadPort.findRoleById(command.expertNo());
        if (!Role.EXPERT.equals(peerRole))
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);

        // 두명이 참여하고 있는 채팅방이 있는지 조회
        Optional<ChatRoom> findChatRoom =
                chatRoomReadPort.findByClientNoAndExpertNo(command.clientNo(), command.expertNo());

        if (findChatRoom.isPresent()) {
            // 있다면 해당 chatRoomNo 반환
            return findChatRoom.get().getChatRoomNo();
        } else {
            // 없다면 채팅방 생성 및 해당 chatRoomNo 반환
            ChatParticipant client = commandMapper.toChatParticipantDomain(command.clientNo());
            ChatParticipant expert = commandMapper.toChatParticipantDomain(command.expertNo());
            ChatRoom createdChatRoom = chatRoomCreatePort.create(List.of(client, expert));
            return createdChatRoom.getChatRoomNo();
        }


    }
}
