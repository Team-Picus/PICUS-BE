package com.picus.core.chat.adapter.out.persistence;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatParticipantEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatParticipantJpaRepository;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import com.picus.core.chat.adapter.out.persistence.mapper.ChatRoomPersistenceMapper;
import com.picus.core.chat.application.port.out.ChatRoomCreatePort;
import com.picus.core.chat.application.port.out.ChatRoomDeletePort;
import com.picus.core.chat.application.port.out.ChatRoomReadPort;
import com.picus.core.chat.application.port.out.ChatRoomUpdatePort;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomCreatePort, ChatRoomReadPort, ChatRoomUpdatePort, ChatRoomDeletePort {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatParticipantJpaRepository chatParticipantJpaRepository;

    private final ChatRoomPersistenceMapper mapper;

    @Override
    public ChatRoom create(List<ChatParticipant> participants) {
        // ChatRoomEntity 저장
        ChatRoomEntity savedChatRoomEntity = chatRoomJpaRepository.save(ChatRoomEntity.builder().build());

        List<ChatParticipantEntity> savedParticipantEntities = new ArrayList<>();
        participants.forEach(chatParticipant -> {
            // ChatParticipant Domain -> Entity
            ChatParticipantEntity chatParticipantEntity = mapper.toChatParticipantEntity(chatParticipant);
            // ChatRoomEntity 바인딩
            chatParticipantEntity.bindChatRoomEntity(savedChatRoomEntity);
            // ChatParticipantEntity 저장
            ChatParticipantEntity saved = chatParticipantJpaRepository.save(chatParticipantEntity);
            savedParticipantEntities.add(saved);
        });

        return mapper.toDomain(savedChatRoomEntity, savedParticipantEntities);
    }

    @Override
    public Optional<ChatRoom> findById(String chatRoomNo) {
        Optional<ChatRoomEntity> optionalChatRoomEntity = chatRoomJpaRepository.findById(chatRoomNo);

        if(optionalChatRoomEntity.isEmpty())
            return Optional.empty();

        ChatRoomEntity chatRoomEntity = optionalChatRoomEntity.get();
        List<ChatParticipantEntity> chatParticipantEntities =
                chatParticipantJpaRepository.findByChatRoomEntity(chatRoomEntity);

        return Optional.of(mapper.toDomain(chatRoomEntity, chatParticipantEntities));
    }

    @Override
    public Optional<ChatRoom> findByClientNoAndExpertNo(String clientNo, String expertNo) {
        Optional<ChatRoomEntity> optionalChatRoomEntity = chatRoomJpaRepository.findByClientNoAndExpertNo(clientNo, expertNo);

        if (optionalChatRoomEntity.isEmpty())
            return Optional.empty();

        ChatRoomEntity chatRoomEntity = optionalChatRoomEntity.get();
        List<ChatParticipantEntity> chatParticipantEntities = chatParticipantJpaRepository.findByChatRoomEntity(chatRoomEntity);

        return optionalChatRoomEntity
                .map(entity -> mapper.toDomain(entity, chatParticipantEntities));
    }

    @Override
    public void updateChatParticipant(ChatParticipant chatParticipant) {
        chatParticipantJpaRepository.findById(chatParticipant.getChatParticipantNo())
                .ifPresent(chatParticipantEntity ->
                        chatParticipantEntity.update(
                                chatParticipant.getIsPinned(),
                                chatParticipant.getIsExited(),
                                chatParticipant.getExitedAt()
                        )
                );
    }

    @Override
    public void delete(String chatRoomNo) {
        // ChatParticipantEntity 삭제
        chatParticipantJpaRepository.deleteByChatRoomEntity_chatRoomNo(chatRoomNo);
        // ChatRoomEntity 삭제
        chatRoomJpaRepository.deleteById(chatRoomNo);
    }
}
