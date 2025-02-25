package com.picus.core.domain.chat.domain.entity.message;

import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDateTime;

@TypeAlias("imageMessage")
public class ImageMessage extends Message {

    private String imageUrl;

    public ImageMessage(Long chattingRoomNo, Long senderId, LocalDateTime sendAt, String imageUrl) {
        super(chattingRoomNo, senderId, sendAt, MessageType.IMAGE, false);
        this.imageUrl = imageUrl;
    }
}
