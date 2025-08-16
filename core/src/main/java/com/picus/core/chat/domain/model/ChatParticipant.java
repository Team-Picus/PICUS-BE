package com.picus.core.chat.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatParticipant {

    private String chatParticipantNo;

    private String userNo;
    private Boolean isPinned;
    private Boolean isExit;
}
