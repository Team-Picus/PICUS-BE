package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.domain.chat.domain.entity.participant.ChatUserId;
import com.picus.core.domain.chat.domain.repository.ChatUserRepository;
import com.picus.core.domain.chat.domain.service.ChatRoomService;
import com.picus.core.domain.user.domain.entity.User;
import com.picus.core.domain.user.domain.repository.UserRepository;
import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.common.image.application.usecase.ImageUploadUseCase;
import com.picus.core.global.common.image.domain.entity.ImageType;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomMetadataUseCase {

    private final ChatRoomService chatRoomService;
    private final ImageUploadUseCase imageUploadUseCase;
    private final SendMessageUseCase sendMessageUseCase;

    @Transactional
    public Long initRoom(Long clientNo, Long expertNo) {
        ChatRoom newRoom = chatRoomService.create(clientNo, expertNo);
        sendMessageUseCase.sendSystemMessage(newRoom.getId(), "예약하시겠습니까?"); // todo: 적절한 워딩으로 변경 예정

        return newRoom.getId();
    }

    public List<ChatRoomRes> readChatRooms(Long userNo, Long last) {
        return chatRoomService.findMyRoomsMetadata(userNo, last)
                .stream()
                .map(this::toChatRoomRes)
                .toList();
    }

    private ChatRoomRes toChatRoomRes(ChatRoomRes tempDto) {
        String profileImageUrl = (tempDto.profileImageId() == null)
                ? null
                : imageUploadUseCase.getImage(tempDto.profileImageId(), ImageType.PROFILE);

        return ChatRoomRes.builder()
                .roomNo(tempDto.roomNo())
                .lastMessageAt(tempDto.lastMessageAt())
                .thumbnailMessage(tempDto.thumbnailMessage())
                .unreadMessageCnt(tempDto.unreadMessageCnt())
                .partnerId(tempDto.partnerId())
                .profileImageId(tempDto.profileImageId())
                .profileImageUrl(profileImageUrl)
                .nickname(tempDto.nickname())
                .build();
    }

}
