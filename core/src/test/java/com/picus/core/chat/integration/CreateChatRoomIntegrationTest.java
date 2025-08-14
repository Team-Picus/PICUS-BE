package com.picus.core.chat.integration;

import com.picus.core.chat.adapter.in.web.data.request.CreateChatRoomRequest;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
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
import org.springframework.http.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateChatRoomIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;

    @AfterEach
    void tearDown() {
        chatRoomJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자는 채팅방을 생성할 수 있다.")
    public void create() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        UserEntity clientUser = createUserEntity("nickname1", Role.CLIENT, "email1@.com", "u1");
        UserEntity expertUser = createUserEntity("nickname2", Role.EXPERT, "email2@.com", "u2");
        commitTestTransaction();

        // given - 웹 요청 셋팅
        CreateChatRoomRequest request = CreateChatRoomRequest.builder()
                .expertNo(expertUser.getUserNo())
                .build();
        HttpEntity<CreateChatRoomRequest> webRequest = settingWebRequest(clientUser, request);


        // when - API 호출
        ResponseEntity<BaseResponse<String>> response = restTemplate.exchange(
                "/api/v1/chats",
                HttpMethod.POST,
                webRequest,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BaseResponse<String> body = response.getBody();
        assertThat(body).isNotNull();

        Optional<ChatRoomEntity> optional = chatRoomJpaRepository.findById(body.getResult());
        assertThat(optional).isPresent();

        ChatRoomEntity chatRoomEntity = optional.get();
        assertThat(chatRoomEntity.getExpertNo()).isEqualTo(request.expertNo());
        assertThat(chatRoomEntity.getClientNo()).isEqualTo(clientUser.getUserNo());
        assertThat(chatRoomEntity.getIsPinned()).isFalse();
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
