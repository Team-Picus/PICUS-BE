package com.picus.core.chat.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChattingRoom {

    private String chattingRoomNo;

    private String clientNo;
    private String expertNo;

    private Boolean isPinned;
}
