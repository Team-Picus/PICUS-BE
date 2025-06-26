package com.picus.core.old.domain.chat.domain.entity;

import com.picus.core.old.global.common.base.BaseEntity;
import com.picus.core.old.global.common.converter.LongSetConverter;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class ChatRoom extends BaseEntity {

    @Id @Tsid
    @Column(name = "room_no")
    private Long id;

    @Convert(converter = LongSetConverter.class)
    private Set<Long> chatUsers = new HashSet<>();

    private LocalDateTime lastMessageAt;

    private String thumbnailMessage;

    public ChatRoom() {
        this.lastMessageAt = LocalDateTime.now();
        this.thumbnailMessage = "대화를 시작합니다.";
    }

    public void updateLastMessage(String thumbnailMessage) {
        this.lastMessageAt = LocalDateTime.now();
        this.thumbnailMessage = thumbnailMessage;
    }

    public void enterUser(Long userNo) {
        this.chatUsers.add(userNo);
    }
}
