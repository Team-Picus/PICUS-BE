package com.picus.core.domain.chat.domain.repository;

import com.picus.core.domain.chat.domain.entity.participant.ChatUser;
import com.picus.core.domain.chat.domain.entity.participant.ChatUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatUserRepository extends JpaRepository<ChatUser, ChatUserId> {

    ChatUser findByUserNo(Long userNo);

    ChatUser findByRoomNo(Long roomNo);
}
