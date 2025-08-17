package com.picus.core.chat.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom {

    private String chatRoomNo;

    @Builder.Default
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    public void exit(ChatParticipant chatParticipant) {
        chatParticipant.exit(LocalDateTime.now());
    }

    public void pin(ChatParticipant chatParticipant) {
        chatParticipant.pin();
    }
}
