package com.picus.core.domain.chat.application.usecase;

import com.picus.core.domain.chat.domain.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomMetadataUseCase {

    private final ChatRoomService chatRoomService;

    public Long initRoom(Long clientNo, Long expertNo) {
        return chatRoomService
                .create(clientNo, expertNo)
                .getId();
    }

}
