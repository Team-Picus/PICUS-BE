package com.picus.core.domain.chat.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatMessageEvent {

    private String roomId;
    private String sender;
    private String content;
    private LocalDateTime createdAt;

    // 기본 생성자와 getter/setter
    public ChatMessageEvent() {}

    public ChatMessageEvent(String roomId, String sender, String content, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.createdAt = createdAt;
    }

}
