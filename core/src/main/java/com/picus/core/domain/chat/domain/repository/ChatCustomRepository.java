package com.picus.core.domain.chat.domain.repository;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;

import java.util.List;

public interface ChatCustomRepository {

    List<ChatRoomRes> findMyChatRoomMetadata(Long userNo, Long lastMessageNo);
}
