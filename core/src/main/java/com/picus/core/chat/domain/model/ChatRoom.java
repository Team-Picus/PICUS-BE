package com.picus.core.chat.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom {

    private String chatRoomNo;

    private String clientNo;
    private String expertNo;

    private Boolean isPinned;
}
