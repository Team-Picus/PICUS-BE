package com.picus.core.chat.integration;

import com.picus.core.chat.adapter.in.web.data.request.ExitChatRoomRequest;
import com.picus.core.chat.adapter.in.web.data.request.PinChatRoomRequest;
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

public class PinChatRoomIntegrationTest extends IntegrationTestSupport {

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
    @DisplayName("사용자는 채팅방을 상단고정처리 할 수 있다.")
    public void exit() throws Exception {
        // given - 데이터베이스 데이터 셋팅
        UserEntity other =
                createUserEntity("other1", Role.EXPERT, "other1@email.com", "other1");
        UserEntity me =
                createUserEntity("me", Role.CLIENT, "me@email.com", "me");

        ChatRoomEntity cr = createChatRoomEntity();
        ChatParticipantEntity cr_me = createChatParticipantEntity(cr, me.getUserNo(), false); // 요청 주체
        ChatParticipantEntity cr_other = createChatParticipantEntity(cr, other.getUserNo(), false);// 채팅 상대방

        commitTestTransaction();

        // given - 요청 셋팅
        PinChatRoomRequest request = PinChatRoomRequest.builder()
                .chatRoomNos(List.of(cr.getChatRoomNo()))
                .build();
        HttpEntity<PinChatRoomRequest> webRequest = settingWebRequest(me, request);

        // when
        ResponseEntity<BaseResponse<Void>> response = restTemplate.exchange(
                "/api/v1/chats/pin",
                HttpMethod.POST,
                webRequest,
                new ParameterizedTypeReference<>() {
                }
        );

        // then - 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BaseResponse<Void> body = response.getBody();
        assertThat(body).isNotNull();

        // then - 현재 사용자는 True로 변경됨
        ChatParticipantEntity uptCr = chatParticipantJpaRepository.findById(cr_me.getChatParticipantNo())
                .orElseThrow();
        assertThat(uptCr.getIsPinned()).isTrue();

        // then - 상대방은 그대로 유지
        ChatParticipantEntity otherCr = chatParticipantJpaRepository.findById(cr_other.getChatParticipantNo())
                .orElseThrow();
        assertThat(otherCr.getIsPinned()).isFalse();
    }

    private ChatRoomEntity createChatRoomEntity() {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder().build();
        return chatRoomJpaRepository.save(chatRoomEntity);
    }

    private ChatParticipantEntity createChatParticipantEntity(ChatRoomEntity chatRoomEntity, String userNo, boolean isPinned) {
        ChatParticipantEntity participantEntity = ChatParticipantEntity.builder()
                .chatRoomEntity(chatRoomEntity)
                .userNo(userNo)
                .isPinned(isPinned)
                .isExited(false)
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
