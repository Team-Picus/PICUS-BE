package com.picus.core.domain.chat.domain.entity.message;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document(value = "message")
@NoArgsConstructor
public abstract class Message {

    @Id
    private Long id;
    private Long roomNo;
    private Long senderNo;
    private LocalDateTime sendAt;
    private MessageType messageType;
    private Boolean isRead;

    public Message(Long roomNo, Long senderNo, MessageType messageType) {
        this.id = (long) Math.random();
        this.roomNo = roomNo;
        this.senderNo = senderNo;
        this.sendAt = LocalDateTime.now();      // default: 현재 시간
        this.messageType = messageType;
        this.isRead = false;    // default: 안읽음
    }

    public void updateIsRead() {
        this.isRead = true;
    }
}
