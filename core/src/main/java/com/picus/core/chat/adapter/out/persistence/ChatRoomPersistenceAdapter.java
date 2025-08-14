package com.picus.core.chat.adapter.out.persistence;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import com.picus.core.chat.adapter.out.persistence.mapper.ChatRoomPersistenceMapper;
import com.picus.core.chat.application.port.out.ChatRoomCreatePort;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomCreatePort, ChatRoomReadPort {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    private final ChatRoomPersistenceMapper mapper;

    @Override
    public ChatRoom create(ChatRoom chatRoom) {
        ChatRoomEntity entity = mapper.toEntity(chatRoom);
        ChatRoomEntity savedEntity = chatRoomJpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ChatRoom> findByClientNoAndExpertNo(String clientNo, String expertNo) {
        return chatRoomJpaRepository.findByClientNoAndExpertNo(clientNo, expertNo)
                .map(mapper::toDomain);
    }
}
