package com.picus.core.chat.adapter.out.persistence.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_participants")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipantEntity {

    @Id
    @Tsid
    private String chatParticipantNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_no", nullable = false)
    private ChatRoomEntity chatRoomEntity;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false)
    private Boolean isPinned;

    @Column(nullable = false)
    private Boolean isExit;

    public void bindChatRoomEntity(ChatRoomEntity chatRoomEntity) {
        this.chatRoomEntity = chatRoomEntity;
    }
}
