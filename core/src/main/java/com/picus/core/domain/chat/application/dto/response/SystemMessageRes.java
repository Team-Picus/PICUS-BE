package com.picus.core.domain.chat.application.dto.response;

import com.picus.core.domain.chat.domain.entity.message.MessageType;
import com.picus.core.domain.chat.domain.entity.message.SystemMessage;
import com.picus.core.domain.chat.domain.entity.message.TextMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SystemMessageRes extends MessageRes {
    static private final MessageType messageType = MessageType.SYSTEM;
    private String content;
    private LocalDateTime sendAt;

    private SystemMessageRes(String id, String content, LocalDateTime sendAt) {
        super(id, messageType);
        this.content = content;
        this.sendAt = sendAt;
    }

    public static MessageRes createRes(SystemMessage message) {
        return new SystemMessageRes(message.getId(), message.getContent(), message.getSendAt());
    }
}
