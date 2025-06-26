package com.picus.core.old.domain.chat.domain.repository;

import com.picus.core.old.domain.chat.domain.entity.ChatRoom;
import com.picus.core.old.domain.chat.infra.repository.ChatCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatCustomRepository {

}
