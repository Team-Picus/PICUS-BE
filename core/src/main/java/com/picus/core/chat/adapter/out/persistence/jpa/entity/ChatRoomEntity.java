package com.picus.core.chat.adapter.out.persistence.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomEntity {

    @Id @Tsid
    private String chatRoomNo;

    @Column(nullable = false)
    private String clientNo;
    @Column(nullable = false)
    private String expertNo;

    @Column(nullable = false)
    private Boolean isPinned;
}
