package com.picus.core.domain.chat.domain.service;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.domain.entity.participant.ChatUserId;
import com.picus.core.domain.chat.domain.repository.ChatCustomRepository;
import com.picus.core.domain.chat.domain.repository.ChatRoomRepository;
import com.picus.core.domain.chat.domain.repository.ChatUserRepository;
import com.picus.core.domain.user.domain.entity.User;
import com.picus.core.domain.user.domain.repository.UserRepository;
import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.common.image.application.usecase.ImageUploadUseCase;
import com.picus.core.global.common.image.domain.entity.ImageType;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatCustomRepository chatCustomRepository;
    private final ImageUploadUseCase imageUploadUseCase;

    public Boolean isExist(Long roomNo) {
        return chatRoomRepository.existsById(roomNo);
    }

    // todo: 파라미터 수정
    public ChatRoom create(Long clientNo, Long exportNo) {
        return chatRoomRepository.save(new ChatRoom());
    }

    @Transactional
    public void updateLastMessage(Long roomNo, String lastMessage) {
        ChatRoom chatRoom = findByRoomNo(roomNo);
        chatRoom.updateLastMessage(lastMessage);
    }

    public ChatRoom findByRoomNo(Long roomNo) {
        return chatRoomRepository.findById(roomNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    @Transactional
    public void delete(Long userNo, Long roomNo) {
        ChatRoom chatRoom = findByRoomNo(roomNo);
//        chatRoom.leave(userNo);
    }

    public List<ChatRoomRes> findMyRoomsMetadata(Long userNo, Long lastMessageNo) {
        return chatCustomRepository.findMyChatRoomMetadata(userNo, lastMessageNo)
                .stream()
                .map(tempDto -> ChatRoomRes.builder()
                        .roomNo(tempDto.roomNo())
                        .lastMessageAt(tempDto.lastMessageAt())
                        .thumbnailMessage(tempDto.thumbnailMessage())
                        .unreadMessageCnt(tempDto.unreadMessageCnt())
                        .partnerId(tempDto.partnerId())
                        .profileImageId(tempDto.profileImageId())
                        .profileImageUrl(
                                tempDto.profileImageId() == null
                                        ? null
                                        : imageUploadUseCase.getImage(tempDto.profileImageId(), ImageType.MESSAGE)
                        )
                        .nickname(tempDto.nickname())
                        .build())
                .toList();
    }

}
