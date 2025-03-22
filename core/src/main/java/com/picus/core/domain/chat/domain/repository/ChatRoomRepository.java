package com.picus.core.domain.chat.domain.repository;

import com.picus.core.domain.chat.domain.entity.ChatRoom;
import com.picus.core.domain.chat.infra.repository.ChatCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatCustomRepository {

}
