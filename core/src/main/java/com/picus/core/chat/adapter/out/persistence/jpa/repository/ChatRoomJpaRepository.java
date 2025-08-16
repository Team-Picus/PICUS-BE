package com.picus.core.chat.adapter.out.persistence.jpa.repository;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, String> {

    @Query("""
                select cp1.chatRoomEntity
                from ChatParticipantEntity cp1 join ChatParticipantEntity cp2 on cp1.chatRoomEntity.chatRoomNo = cp2.chatRoomEntity.chatRoomNo
                where cp1.userNo = :clientNo and cp2.userNo = :expertNo
            """)
    Optional<ChatRoomEntity> findByClientNoAndExpertNo(String clientNo, String expertNo);

}
