package com.picus.core.chat.adapter.out.persistence.jpa.repository;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.MessageReadStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageReadStatusJpaRepository extends JpaRepository<MessageReadStatusEntity, String> {

    List<MessageReadStatusEntity> findByMessageNo(String messageNo);

    // 특정 사용자의 해당 채팅방 안 읽은 메시지 수
    long countByChatRoomNoAndUserNoAndIsReadFalse(String chatRoomNo, String userNo);
}
