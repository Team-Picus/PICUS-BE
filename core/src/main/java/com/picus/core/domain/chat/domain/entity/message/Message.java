package com.picus.core.domain.chat.domain.entity.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(value = "message")
@NoArgsConstructor
public abstract class Message {

    private Long id;
    private Long chattingRoomNo;
    private Long senderId;
    private LocalDateTime sendAt;
    private MessageType messageType;
    private Boolean isRead;

    public Message(Long chattingRoomNo, Long senderId, LocalDateTime sendAt, MessageType messageType, Boolean isRead) {
        this.chattingRoomNo = chattingRoomNo;
        this.senderId = senderId;
        this.sendAt = sendAt;
        this.messageType = messageType;
    }
}
