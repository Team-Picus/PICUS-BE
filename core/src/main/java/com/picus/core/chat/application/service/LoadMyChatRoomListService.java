package com.picus.core.chat.application.service;

import com.picus.core.chat.application.port.in.LoadMyChatRoomListUseCase;
import com.picus.core.chat.application.port.in.result.LoadMyChatRoomListResult;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.application.port.out.MessageReadPort;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.chat.domain.model.vo.*;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.application.port.out.join_dto.UserWithProfileImageDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._INTERNAL_SERVER_ERROR;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
public class LoadMyChatRoomListService implements LoadMyChatRoomListUseCase {

    private final ChatRoomReadPort chatRoomReadPort;
    private final MessageReadPort messageReadPort;
    private final UserReadPort userReadPort;

    @Override
    public List<LoadMyChatRoomListResult> load(String currentUserNo) {

        // 해당 사용자가 속한 채팅방 목록 조회
        List<ChatRoom> chatRooms = chatRoomReadPort.findByUserNo(currentUserNo);

        List<LoadMyChatRoomListResult> results = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            // 각 채팅방의 마지막 메시지 조회.
            Message lastMessage = messageReadPort.findLastMessageByChatRoomNo(chatRoom.getChatRoomNo())
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));

            // 안읽은 메시지 수 조회
            long unReadCount = messageReadPort.countUnReadMessageByChatRoomNoAndUserNo(chatRoom.getChatRoomNo(), currentUserNo);

            // 각 채팅방의 상대방 정보 조회(닉네임, 프로필이미지)
            String peerUserNo = getPeer(currentUserNo, chatRoom.getChatParticipants())
                    .getUserNo(); // 상대방 UserNo 추출

            UserWithProfileImageDto peerInfo = userReadPort.findUserInfoByUserNo(peerUserNo)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));


            // Result 조립
            LoadMyChatRoomListResult result = LoadMyChatRoomListResult.builder()
                    .peerNickname(peerInfo.nickname())
                    .peerProfileUrl("") // TODO: key -> url 변환
                    .lastMessage(getLastMessageContent(lastMessage.getContent()))
                    .lastMessageSentAt(lastMessage.getCreatedAt())
                    .isPinned(getMe(currentUserNo, chatRoom.getChatParticipants()).getIsPinned())
                    .unreadMessageCount(unReadCount)
                    .build();
            results.add(result);
        }
        return results;
    }

    private ChatParticipant getPeer(String currentUserNo, List<ChatParticipant> chatParticipants) {
        return chatParticipants.stream()
                .filter(chatParticipant -> !chatParticipant.getUserNo().equals(currentUserNo))
                .findAny()
                .orElseThrow(() -> new RestApiException(_INTERNAL_SERVER_ERROR));
    }

    private ChatParticipant getMe(String currentUserNo, List<ChatParticipant> chatParticipants) {
        return chatParticipants.stream()
                .filter(chatParticipant -> chatParticipant.getUserNo().equals(currentUserNo))
                .findAny()
                .orElseThrow(() -> new RestApiException(_INTERNAL_SERVER_ERROR));
    }

    private String getLastMessageContent(MessageContent content) {
        switch (content) {
            case TextContent textContent:
                return textContent.text();
            case FileContent fileContent:
                return "파일 메시지입니다.";
            case ImageContent imageContent:
                return "사진 메시지입니다.";
            case ReservationContent reservationContent:
                return "예약관련 메시지입니다.";
            default:
                throw new RestApiException(_INTERNAL_SERVER_ERROR);
        }
    }


}
