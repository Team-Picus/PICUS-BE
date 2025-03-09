package com.picus.core.domain.chat.application.dto.response;

import com.picus.core.domain.chat.domain.entity.message.MessageType;
import com.picus.core.domain.chat.domain.entity.message.TextMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TextMessageRes extends MessageRes {
    static private final MessageType messageType = MessageType.TEXT;
    private Long senderNo;
    private String content;
    private LocalDateTime sendAt;
    private int unreadCnt;

    private TextMessageRes(Long id, Long senderNo, String content, LocalDateTime sendAt, int unreadCnt) {
        super(id, messageType);
        this.senderNo = senderNo;
        this.content = content;
        this.sendAt = sendAt;
        this.unreadCnt = unreadCnt;
    }

    public static MessageRes createRes(TextMessage message, int unreadCnt) {
        return new TextMessageRes(message.getId(), message.getSenderNo(), message.getContent(), message.getSendAt(), unreadCnt);
    }
}
