package com.picus.core.domain.chat.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ChattingRoom {

    @Id @Tsid
    @Column(name = "chatting_room_no")
    private Long id;

    private Long clientNo;

    private Long expertNo;

    private LocalDateTime lastMessageAt;

    private Long thumbnailMessageId;
}
