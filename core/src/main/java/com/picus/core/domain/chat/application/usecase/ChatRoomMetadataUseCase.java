package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.domain.service.ChatRoomService;
import com.picus.core.domain.shared.image.application.usecase.ImageUploadUseCase;
import com.picus.core.domain.shared.image.domain.entity.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
