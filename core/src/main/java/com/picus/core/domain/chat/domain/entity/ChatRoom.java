package com.picus.core.domain.chat.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id @Tsid
    @Column(name = "room_no")
    private Long id;

    private Long clientNo;

    private Long expertNo;

    private LocalDateTime lastMessageAt;

    private String thumbnailMessage;

    public ChatRoom(Long clientNo, Long expertNo) {
        this.clientNo = clientNo;
        this.expertNo = expertNo;
    }

    public void updateLastMessage(String thumbnailMessage) {
        this.lastMessageAt = LocalDateTime.now();
        this.thumbnailMessage = thumbnailMessage;
    }
}
