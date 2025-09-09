package com.picus.core.chat.application.service;

import com.picus.core.chat.application.port.in.SearchMyChatRoomUseCase;
import com.picus.core.chat.application.port.in.result.SearchMyChatRoomResult;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._INTERNAL_SERVER_ERROR;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchMyChatRoomService implements SearchMyChatRoomUseCase {

    private final ChatRoomReadPort chatRoomReadPort;
    private final MessageReadPort messageReadPort;
    private final UserReadPort userReadPort;

    @Override
    public List<SearchMyChatRoomResult> search(String currentUserNo, String keyword) {

        // 해당 사용자가 속한 채팅방 목록 조회. 상단 고정처리한 채팅방부터 우선적으로 가져오기
        List<ChatRoom> chatRooms = chatRoomReadPort.findByUserNo(currentUserNo);

        List<SearchMyChatRoomResult> results = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            // 채팅방에서 키워드가 포함되는 메시지 조회 (여러 개가 있는 경우 최근 메시지 조회)
            Optional<Message> message = messageReadPort.findLastByChatRoomNoAndKeywordContaining(chatRoom.getChatRoomNo(), keyword);

            // 키워드가 포함된 메시지가 존재하면
            message.ifPresent(matchingMessage -> {
                // 안 읽은 메시지 수 조회
                long unReadCount = messageReadPort.countUnReadMessageByChatRoomNoAndUserNo(chatRoom.getChatRoomNo(), currentUserNo);

                // 채팅방의 상대방 정보 조회(닉네임, 프로필이미지)
                String peerUserNo = getPeer(currentUserNo, chatRoom.getChatParticipants())
                        .getUserNo(); // 상대방 UserNo 추출

                UserWithProfileImageDto peerInfo = userReadPort.findUserInfoByUserNo(peerUserNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));

                // Result 조립
                SearchMyChatRoomResult result = SearchMyChatRoomResult.builder()
                        .peerNickname(peerInfo.nickname())
                        .peerProfileUrl("") // TODO: key -> url 변환
                        .matchingMessage(getMessageContent(matchingMessage.getContent()))
                        .matchingMessageSentAt(matchingMessage.getCreatedAt())
                        .isPinned(getMe(currentUserNo, chatRoom.getChatParticipants()).getIsPinned())
                        .unreadMessageCount(unReadCount)
                        .build();

                results.add(result);
            });
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
    private String getMessageContent(MessageContent content) {
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
