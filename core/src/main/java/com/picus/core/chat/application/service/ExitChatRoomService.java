package com.picus.core.chat.application.service;

import com.picus.core.chat.application.port.in.ExitChatRoomUseCase;
import com.picus.core.chat.application.port.in.command.ExitChatRoomCommand;
import com.picus.core.chat.application.port.out.ChatRoomDeletePort;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.application.port.out.ChatRoomUpdatePort;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._FORBIDDEN;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class ExitChatRoomService implements ExitChatRoomUseCase {

    private final ChatRoomReadPort chatRoomReadPort;
    private final ChatRoomUpdatePort chatRoomUpdatePort;
    private final ChatRoomDeletePort chatRoomDeletePort;

    // TODO: 동시성 처리
    @Override
    public void exit(ExitChatRoomCommand command) {
        List<ChatRoom> chatRooms = chatRoomReadPort.findAllByIds(command.chatRoomNos());
        for (ChatRoom chatRoom : chatRooms) {

            List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();
            // 현재 사용자가 채팅방에 들어가 있는지 검증
            ChatParticipant me = chatParticipants.stream()
                    .filter(chatParticipant -> chatParticipant.getUserNo().equals(command.currentUserNo()))
                    .findAny()
                    .orElseThrow(() -> new RestApiException(_FORBIDDEN));

            if (isChatRoomEmptyExceptMe(chatParticipants, me.getUserNo())) {
                // 상대방이 채팅방을 나가 있으면(isExit이 True라면) 채팅방 삭제
                chatRoomDeletePort.delete(chatRoom.getChatRoomNo());
            } else {
                // 상대방이 채팅방에 남아있다면 그냥 ChatParticipant의 isExit만 수정
                chatRoom.exit(me);
                chatRoomUpdatePort.updateChatParticipant(me);
            }
        }
    }

    private boolean isChatRoomEmptyExceptMe(List<ChatParticipant> chatParticipants, String me) {
        return chatParticipants.stream()
                .anyMatch(chatParticipant ->
                        !chatParticipant.getUserNo().equals(me)
                                && chatParticipant.getIsExited());
    }
}
