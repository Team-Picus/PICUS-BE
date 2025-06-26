package com.picus.core.old.domain.chat.infra.repository;

import com.picus.core.old.domain.chat.application.dto.response.ChatRoomRes;

import java.util.List;

public interface ChatCustomRepository {

    List<ChatRoomRes> findMyChatRoomMetadata(Long userNo, Long lastMessageNo);
}
