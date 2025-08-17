package com.picus.core.chat.application.service;

import com.picus.core.chat.application.port.in.PinChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.PinChatRoomCommand;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.application.port.out.ChatRoomUpdatePort;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._FORBIDDEN;

@UseCase
@RequiredArgsConstructor
@Transactional
public class PinChatRoomService implements PinChatRoomUseCase {

    private final ChatRoomReadPort chatRoomReadPort;
    private final ChatRoomUpdatePort chatRoomUpdatePort;

    @Override
    public void pin(PinChatRoomCommand command) {
        // chatRoom List 조회
        List<ChatRoom> chatRooms = chatRoomReadPort.findAllByIds(command.chatRoomNos());
        List<ChatParticipant> updatedChatParticipants = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();

            // 현재 사용자가 채팅방의 참여자인지 검증
            ChatParticipant me = chatParticipants.stream()
                    .filter(chatParticipant -> chatParticipant.getUserNo().equals(command.currentUserNo()))
                    .findAny()
                    .orElseThrow(() -> new RestApiException(_FORBIDDEN));

            // 고정처리
            chatRoom.pin(me);
            updatedChatParticipants.add(me);
        }
        // 데이터베이스 일괄 업데이트
        chatRoomUpdatePort.bulkUpdateChatParticipant(updatedChatParticipants);
    }
}
