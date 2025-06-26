package com.picus.core.old.domain.chat.domain.repository;

import com.picus.core.old.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.old.domain.chat.domain.entity.participant.ChatUserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, ChatUserId> {

    ChatUser findByUserNo(Long userNo);

    ChatUser findByRoomNo(Long roomNo);
}
