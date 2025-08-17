package com.picus.core.chat.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatParticipant {

    private String chatParticipantNo;

    private String userNo;
    private Boolean isPinned;
    private Boolean isExited;
    private LocalDateTime exitedAt;

    public void exit(LocalDateTime now) {
        this.isExited = true;
        this.exitedAt = now;
    }
}
