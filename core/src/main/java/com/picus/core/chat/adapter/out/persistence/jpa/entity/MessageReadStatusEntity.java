package com.picus.core.chat.adapter.out.persistence.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

@Entity
@Table(name = "message_read_statuses")
public class MessageReadStatusEntity {

    @Id @Tsid
    private String messageReadStatusNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_no")
    private ChatRoomEntity chatRoomEntity; // 조회 편의를 위해

    @Column(nullable = false)
    private String messageNo;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false)
    private Boolean isRead;
}
