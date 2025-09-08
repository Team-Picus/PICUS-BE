package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.response.LoadMyChatRoomListResponse;
import com.picus.core.chat.application.port.in.result.LoadMyChatRoomListResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadMyChatRoomListWebMapper {

    public LoadMyChatRoomListResponse toResponse(List<LoadMyChatRoomListResult> results) {
        return LoadMyChatRoomListResponse.builder()
                .chatRooms(
                        results.stream()
                                .map(result -> LoadMyChatRoomListResponse.ChatRoomResponse.builder()
                                        .peerNickname(result.peerNickname())
                                        .peerProfileUrl(result.peerProfileUrl())
                                        .lastMessage(result.lastMessage())
                                        .lastMessageSentAt(result.lastMessageSentAt())
                                        .isPinned(result.isPinned())
                                        .unreadMessageCount(result.unreadMessageCount())
                                        .build()
                                ).toList()
                ).build();
    }
}
