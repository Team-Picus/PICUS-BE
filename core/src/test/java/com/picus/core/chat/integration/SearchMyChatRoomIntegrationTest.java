package com.picus.core.chat.integration;

import com.picus.core.chat.adapter.in.web.data.response.LoadMyChatRoomListResponse;
import com.picus.core.chat.adapter.in.web.data.response.SearchMyChatRoomResponse;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatParticipantEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.MessageReadStatusEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatParticipantJpaRepository;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.MessageReadStatusJpaRepository;
import com.picus.core.chat.adapter.out.persistence.mongo.document.MessageDocument;
import com.picus.core.chat.adapter.out.persistence.mongo.repository.MessageMongoRepository;
import com.picus.core.chat.domain.model.vo.MessageType;
import com.picus.core.chat.domain.model.vo.TextContent;
import com.picus.core.shared.IntegrationTestSupport;
import com.picus.core.shared.common.BaseResponse;
import com.picus.core.user.adapter.out.persistence.entity.ProfileImageEntity;
import com.picus.core.user.adapter.out.persistence.entity.UserEntity;
import com.picus.core.user.adapter.out.persistence.repository.ProfileImageJpaRepository;
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
import static org.assertj.core.api.Assertions.tuple;

public class SearchMyChatRoomIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ProfileImageJpaRepository profileImageJpaRepository;
    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;
    @Autowired
    private ChatParticipantJpaRepository chatParticipantJpaRepository;
    @Autowired
    private MessageMongoRepository messageMongoRepository;
    @Autowired
    private MessageReadStatusJpaRepository messageReadStatusJpaRepository;

    @Test
    @DisplayName("사용자는 특정 키워드를 입력하여 자신의 채팅방 중 해당 키워드와 일치하는 메시지가 있는 채팅방을 검색할 수 있다.")
    public void search() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        UserEntity me = createUserEntity("me");
        UserEntity opponent1 = createUserEntity("opponent1");
        UserEntity opponent2 = createUserEntity("opponent2");
        // 채팅방 1
        ChatRoomEntity cr1 = createChatRoomEntity();
        createChatParticipantEntity(cr1, me.getUserNo(), false, false);
        createChatParticipantEntity(cr1, opponent1.getUserNo(), false, false);

        // 채팅방 1 메시지 저장
        createTextMessageDocument(cr1.getChatRoomNo(), me.getUserNo(), opponent1.getUserNo(), "회의 준비됐나요?");
        Thread.sleep(100);
        createTextMessageDocument(cr1.getChatRoomNo(), opponent1.getUserNo(), me.getUserNo(), "회의 미뤄졌습니다.");

        // 채팅방 2
        ChatRoomEntity cr2 = createChatRoomEntity();
        createChatParticipantEntity(cr2, me.getUserNo(), false, false);
        createChatParticipantEntity(cr2, opponent2.getUserNo(), false, false);

        // 채팅방 2 메시지 저장
        createTextMessageDocument(cr2.getChatRoomNo(), opponent2.getUserNo(), me.getUserNo(), "점심 뭐 먹을까");
        Thread.sleep(100);
        createTextMessageDocument(cr2.getChatRoomNo(), me.getUserNo(), opponent2.getUserNo(), "자장면");

        commitTestTransaction();

        // given - 요청 객체 셋팅
        HttpEntity<Object> request = settingWebRequest(me, null);

        String keyword = "회의"; // 검색어

        // when
        ResponseEntity<BaseResponse<SearchMyChatRoomResponse>> response = restTemplate.exchange(
                "/api/v1/chats/search?keyword={keyword}",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                keyword
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BaseResponse<SearchMyChatRoomResponse> body = response.getBody();
        assertThat(body).isNotNull();

        SearchMyChatRoomResponse result = body.getResult();
        assertThat(result.chatRooms()).hasSize(1)
                .extracting(
                        SearchMyChatRoomResponse.ChatRoomResponse::peerNickname,
                        SearchMyChatRoomResponse.ChatRoomResponse::peerProfileUrl,
                        SearchMyChatRoomResponse.ChatRoomResponse::matchingMessage,
                        SearchMyChatRoomResponse.ChatRoomResponse::isPinned,
                        SearchMyChatRoomResponse.ChatRoomResponse::unreadMessageCount
                ).contains(
                        tuple("opponent1", "", "회의 미뤄졌습니다.", false, 1L)
                );
    }



    @AfterEach
    void tearDown() {
        messageReadStatusJpaRepository.deleteAllInBatch();
        messageMongoRepository.deleteAll();
        chatParticipantJpaRepository.deleteAllInBatch();
        chatRoomJpaRepository.deleteAllInBatch();
        profileImageJpaRepository.deleteAllInBatch();
        userJpaRepository.deleteAllInBatch();
    }


    private UserEntity createUserEntity(String nickname) {
        UserEntity userEntity = UserEntity.builder()
                .name("이름")
                .nickname(nickname)
                .tel("01012345678")
                .role(Role.CLIENT)
                .email(String.format("%s@example.com", nickname))
                .providerId(nickname)
                .provider(Provider.KAKAO)
                .reservationHistoryCount(5)
                .followCount(10)
                .myMoodboardCount(2)
                .build();
        userJpaRepository.save(userEntity);
        userEntity.assignExpertNo(userEntity.getUserNo());
        createProfileImageEntity(nickname, userEntity.getUserNo());
        return userEntity;
    }
    private ProfileImageEntity createProfileImageEntity(String fileKey, String userNo) {
        ProfileImageEntity profileImageEntity = ProfileImageEntity.builder()
                .file_key(fileKey)
                .userNo(userNo)
                .build();
        return profileImageJpaRepository.save(profileImageEntity);
    }

    private ChatRoomEntity createChatRoomEntity() {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder().build();
        return chatRoomJpaRepository.save(chatRoomEntity);
    }

    private MessageDocument createTextMessageDocument(String chatRoomNo, String senderNo, String receiverNo, String text) {
        // Message Document 저장
        MessageDocument message = MessageDocument.builder()
                .chatRoomNo(chatRoomNo)
                .senderNo(senderNo)
                .messageType(MessageType.TEXT)
                .content(TextContent.builder().text(text).build())
                .build();
        messageMongoRepository.save(message);

        // MessageReadStatusEntity 저장
        MessageReadStatusEntity readStatus_me = MessageReadStatusEntity.builder()
                .chatRoomNo(chatRoomNo)
                .messageNo(message.getMessageNo().toHexString())
                .userNo(senderNo)
                .isRead(true)
                .build();
        MessageReadStatusEntity readStatus_opp = MessageReadStatusEntity.builder()
                .chatRoomNo(chatRoomNo)
                .messageNo(message.getMessageNo().toHexString())
                .userNo(receiverNo)
                .isRead(false)
                .build();
        messageReadStatusJpaRepository.saveAll(List.of(readStatus_me, readStatus_opp));
        return message;
    }

    private ChatParticipantEntity createChatParticipantEntity(ChatRoomEntity chatRoomEntity, String userNo, boolean isPinned, boolean isExited) {
        ChatParticipantEntity participantEntity = ChatParticipantEntity.builder()
                .chatRoomEntity(chatRoomEntity)
                .userNo(userNo)
                .isPinned(isPinned)
                .isExited(isExited)
                .build();
        return chatParticipantJpaRepository.save(participantEntity);
    }
}
