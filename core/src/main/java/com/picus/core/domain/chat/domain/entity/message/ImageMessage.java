package com.picus.core.domain.chat.domain.entity.message;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("imageMessage")
public class ImageMessage extends Message {

    private Long imageId;

    public ImageMessage(Long roomNo, Long senderNo, Long imageId) {
        super(roomNo, senderNo, MessageType.IMAGE);
        this.imageId = imageId;
    }
}
