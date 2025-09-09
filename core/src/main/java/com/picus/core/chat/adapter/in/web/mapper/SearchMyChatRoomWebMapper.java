package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.response.SearchMyChatRoomResponse;
import com.picus.core.chat.application.port.in.result.SearchMyChatRoomResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchMyChatRoomWebMapper {

    public SearchMyChatRoomResponse toResponse(List<SearchMyChatRoomResult> results) {
        return SearchMyChatRoomResponse.builder()
                .chatRooms(
                        results.stream()
                                .map(result -> SearchMyChatRoomResponse.ChatRoomResponse.builder()
                                        .peerNickname(result.peerNickname())
                                        .peerProfileUrl(result.peerProfileUrl())
                                        .matchingMessage(result.matchingMessage())
                                        .matchingMessageSentAt(result.matchingMessageSentAt())
                                        .isPinned(result.isPinned())
                                        .unreadMessageCount(result.unreadMessageCount())
                                        .build()
                                ).toList()
                ).build();
    }

}
