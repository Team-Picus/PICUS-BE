package com.picus.core.chat.domain.model;

import com.picus.core.chat.domain.model.vo.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    private String messageNo;

    private String chatRoomNo;
    private String senderNo;
    private String recipientNo;
    private Map<String, Object> content;
    private MessageType messageType;
    private Boolean isRead;
}
