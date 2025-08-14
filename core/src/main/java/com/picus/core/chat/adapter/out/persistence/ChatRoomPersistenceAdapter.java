package com.picus.core.chat.adapter.out.persistence;

import com.picus.core.chat.application.port.out.ChatRoomCreatePort;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomCreatePort, ChatRoomReadPort {

    @Override
    public ChatRoom create(ChatRoom chatRoom) {
        return null;
    }

    @Override
    public Optional<ChatRoom> findByClientNoAndExpertNo(String clientNo, String expertNo) {
        return Optional.empty();
    }
}
