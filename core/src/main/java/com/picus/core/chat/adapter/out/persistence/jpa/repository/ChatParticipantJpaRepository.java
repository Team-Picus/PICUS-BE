package com.picus.core.chat.adapter.out.persistence.jpa.repository;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatParticipantEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantJpaRepository extends JpaRepository<ChatParticipantEntity, String> {

    List<ChatParticipantEntity> findByChatRoomEntity(ChatRoomEntity chatRoomEntity);

    void deleteByChatRoomEntity_chatRoomNo(String chatRoomNo);
}
