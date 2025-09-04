package com.picus.core.chat.adapter.out.persistence.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_read_statuses")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageReadStatusEntity {

    @Id @Tsid
    private String messageReadStatusNo;

    @Column(nullable = false)
    private String chatRoomNo; // 조회 편의를 위해

    @Column(nullable = false)
    private String messageNo;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false)
    private Boolean isRead;

    public void bindMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }
}
