package com.picus.core.chat.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatting_rooms")
public class ChattingRoomEntity {

    @Id @Tsid
    private String chattingRoomNo;

    private Boolean isPinned;
}
