package com.picus.core.chat.adapter.out.persistence.jpa.repository;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatParticipantEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatParticipantJpaRepository extends JpaRepository<ChatParticipantEntity, String> {

    List<ChatParticipantEntity> findByChatRoomEntity(ChatRoomEntity chatRoomEntity);

    // 특정 유저가 참여 중인 채팅방 가져오기. 상단 고정처리한 채팅방부터 우선적으로 가져오기
    @Query("select cp.chatRoomEntity " +
            "from ChatParticipantEntity cp " +
            "where cp.userNo = :userNo and cp.isExited = false")
    List<ChatRoomEntity> findChatRoomsByUserNo(@Param("userNo") String userNo);

    // 여러 채팅방에 속한 참여자들 조회
    List<ChatParticipantEntity> findByChatRoomEntity_ChatRoomNoIn(List<String> chatRoomNos);

    void deleteByChatRoomEntity_chatRoomNo(String chatRoomNo);
}
