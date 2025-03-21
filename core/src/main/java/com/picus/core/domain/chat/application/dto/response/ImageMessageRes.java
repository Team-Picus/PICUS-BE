package com.picus.core.domain.chat.application.dto.response;

import com.picus.core.domain.chat.domain.entity.message.ImageMessage;
import com.picus.core.domain.chat.domain.entity.message.MessageType;
import com.picus.core.domain.chat.domain.entity.message.TextMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ImageMessageRes extends MessageRes {
    static private final MessageType messageType = MessageType.IMAGE;
    private Long senderNo;
    private Long imageId;
    private LocalDateTime sendAt;
    private int unreadCnt;

    private ImageMessageRes(String id, Long senderNo, Long imageId, LocalDateTime sendAt, int unreadCnt) {
        super(id, messageType);
        this.senderNo = senderNo;
        this.imageId = imageId;
        this.sendAt = sendAt;
        this.unreadCnt = unreadCnt;
    }

    public static MessageRes createRes(ImageMessage message, int unreadCnt) {
        return new ImageMessageRes(message.getId(), message.getSenderNo(), message.getImageId(), message.getSendAt(), unreadCnt);
    }
}
