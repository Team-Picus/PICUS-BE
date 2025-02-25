package com.picus.core.domain.chat.domain.entity.message;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDateTime;

@Getter
@TypeAlias("textMessage")
public class TextMessage extends Message {

    private String content;

    public TextMessage(Long chattingRoomNo, Long senderId, LocalDateTime sendAt, String content) {
        super(chattingRoomNo, senderId, sendAt, MessageType.TEXT, false);
        this.content = content;
    }
}
