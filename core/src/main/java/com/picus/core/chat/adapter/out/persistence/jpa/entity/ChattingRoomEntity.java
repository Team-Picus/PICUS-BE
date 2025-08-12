package com.picus.core.chat.adapter.out.persistence.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatting_rooms")
public class ChattingRoomEntity {

    @Id @Tsid
    private String chattingRoomNo;

    @Column(nullable = false)
    private String clientNo;
    @Column(nullable = false)
    private String expertNo;

    @Column(nullable = false)
    private Boolean isPinned;
}
