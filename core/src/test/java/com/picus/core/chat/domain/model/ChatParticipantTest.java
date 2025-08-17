package com.picus.core.chat.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChatParticipantTest {

    @Test
    @DisplayName("ChatParticipant의 isExit을 true로 변경한다.")
    public void exit() throws Exception {
        // given
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .isExited(false)
                .build();

        // when
        chatParticipant.exit(LocalDateTime.now());

        // then
        assertThat(chatParticipant.getIsExited()).isTrue();
    }
    @Test
    @DisplayName("ChatPariticipant의 isPinned을 True로 변경한다.")
    public void pin() throws Exception {
        // given
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .isPinned(false)
                .build();

        // when
        chatParticipant.pin();

        // then
        assertThat(chatParticipant.getIsPinned()).isTrue();
    }

}