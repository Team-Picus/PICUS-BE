package com.picus.core.chat.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MessageReadStatus {

    private String messageReadStatusNo;

    private String chatRoomNo;
    private String userNo;
    private Boolean isRead;
}
