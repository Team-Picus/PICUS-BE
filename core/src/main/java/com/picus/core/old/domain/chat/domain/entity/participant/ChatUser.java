package com.picus.core.old.domain.chat.domain.entity.participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@IdClass(ChatUserId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatUser {

    @Id @Column(nullable = false)
    private Long userNo;

    @Id @Column(nullable = false)
    private Long roomNo;

    private LocalDateTime lastEntryTime;

    private Integer unreadCnt;

    public ChatUser(Long userNo, Long roomNo) {
        this.userNo = userNo;
        this.roomNo = roomNo;
        this.lastEntryTime = LocalDateTime.now();
    }

    public void updateLastEntryTime() {
        this.lastEntryTime = LocalDateTime.now();
    }
}
