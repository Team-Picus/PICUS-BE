package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.domain.chat.domain.service.ChatRoomService;
import com.picus.core.domain.chat.domain.service.ChatUserService;
import com.picus.core.domain.expert.domain.service.ExpertService;
import com.picus.core.domain.shared.image.application.usecase.ImageUploadUseCase;
import com.picus.core.domain.shared.image.domain.entity.ImageType;
import com.picus.core.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class ChatRoomMetadataUseCase {

    private final ChatRoomService chatRoomService;
    private final ImageUploadUseCase imageUploadUseCase;
    private final ExpertService expertService;
    private final ChatUserService chatUserService;

    @Transactional
    public Long initRoom(Long clientNo, Long expertNo) {
        if(!expertService.isExist(expertNo) || clientNo.equals(expertNo))
            throw new RestApiException(_BAD_REQUEST);

        ChatRoom newRoom = chatRoomService.create();
        chatUserService.create(clientNo, newRoom);
        chatUserService.create(expertNo, newRoom);

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
