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
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomMetadataUseCase {

    private final ChatRoomService chatRoomService;
    private final ImageUploadUseCase imageUploadUseCase;

    public Long initRoom(Long clientNo, Long expertNo) {
        return chatRoomService
                .create(clientNo, expertNo)
                .getId();
    }

    public List<ChatRoomRes> readChatRooms(Long userNo, Long last) {
//        if (last.isEmpty()) {
//            return chatRoomService.findMyRoomsMetadata(userNo);
//        }
        return chatRoomService.findMyRoomsMetadata(userNo, last);
    }

    public void leaveChatRoom(Long userNo, Long roomNo) {
        chatRoomService.delete(userNo, roomNo);
    }
}
