package com.picus.core.chat.integration;

import com.picus.core.chat.adapter.in.web.data.request.ExitChatRoomRequest;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatParticipantEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatParticipantJpaRepository;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import com.picus.core.shared.IntegrationTestSupport;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.UserJpaRepository;
import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExitChatRoomIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;
    @Autowired
    private ChatParticipantJpaRepository chatParticipantJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
    void tearDown() {
        chatParticipantJpaRepository.deleteAllInBatch();
        chatRoomJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 채팅방을 나갈 수 있다.")
    public void exit() throws Exception {
        // given - 데이터베이스 데이터 셋팅
        UserEntity other1 =
                createUserEntity("other1", Role.EXPERT, "other1@email.com", "other1");
        UserEntity other2 =
                createUserEntity("other2", Role.EXPERT, "other2@email.com", "other2");
        UserEntity me =
                createUserEntity("me", Role.CLIENT, "me@email.com", "me");

        ChatRoomEntity cr1 = createChatRoomEntity();
        ChatParticipantEntity cr1_me = createChatParticipantEntity(cr1, me.getUserNo(), false);// 요청 주체
        createChatParticipantEntity(cr1, other1.getUserNo(), false); // 채팅방에 있음

        ChatRoomEntity cr2 = createChatRoomEntity();
        ChatParticipantEntity cr2_me = createChatParticipantEntity(cr2, me.getUserNo(), false);// 요청 주체
        createChatParticipantEntity(cr2, other2.getUserNo(), true); // 채팅방에서 나가 있음

        commitTestTransaction();

        // given - 요청 셋팅
        ExitChatRoomRequest request = ExitChatRoomRequest.builder()
                .chatRoomNos(List.of(cr1.getChatRoomNo(), cr2.getChatRoomNo()))
                .build();
        HttpEntity<ExitChatRoomRequest> webRequest = settingWebRequest(me, request);

        // when
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/chats/exit",
                HttpMethod.POST,
                webRequest,
                new ParameterizedTypeReference<>() {
                }
        );

        // then - 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BaseResponse<Void> body = response.getBody();
        assertThat(body).isNotNull();

        // then - cr1은 상대방이 채팅방에 있으므로 채팅방은 그대로이고 ChatParticipant의 isExited, exitedAt만 변경됨
        ChatParticipantEntity uptCr1 = chatParticipantJpaRepository.findById(cr1_me.getChatParticipantNo())
                .orElseThrow();

        assertThat(uptCr1.getIsExited()).isTrue();
        assertThat(uptCr1.getExitedAt()).isNotNull();

        // then - cr2는 상대방이 채팅방에서 나가있었으므로 채팅방이 삭제됨.
        assertThat(chatRoomJpaRepository.findById(cr2.getChatRoomNo())).isNotPresent();
    }

    private ChatRoomEntity createChatRoomEntity() {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder().build();
        return chatRoomJpaRepository.save(chatRoomEntity);
    }

    private ChatParticipantEntity createChatParticipantEntity(ChatRoomEntity chatRoomEntity, String userNo, boolean isExited) {
        ChatParticipantEntity participantEntity = ChatParticipantEntity.builder()
                .chatRoomEntity(chatRoomEntity)
                .userNo(userNo)
                .isPinned(false)
                .isExited(isExited)
                .build();
        return chatParticipantJpaRepository.save(participantEntity);
    }

    private UserEntity createUserEntity(String nickname, Role role, String email, String providerId) {
        UserEntity userEntity = UserEntity.builder()
                .name("이름")
                .nickname(nickname)
                .tel("01012345678")
                .role(role)
                .email(email)
                .providerId(providerId)
                .provider(Provider.KAKAO)
                .reservationHistoryCount(5)
                .followCount(10)
                .myMoodboardCount(2)
                .build();
        return userJpaRepository.save(userEntity);
    }
}
