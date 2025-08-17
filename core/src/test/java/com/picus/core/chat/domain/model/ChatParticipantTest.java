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

}