package com.picus.core.chat.adapter.out.persistence.jpa.repository;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, String> {

    Optional<ChatRoomEntity> findByClientNoAndExpertNo(String clientNo, String expertNo);
}
