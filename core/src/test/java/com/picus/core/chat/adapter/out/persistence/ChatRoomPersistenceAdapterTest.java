package com.picus.core.chat.adapter.out.persistence;


import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatParticipantEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatParticipantJpaRepository;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import com.picus.core.chat.adapter.out.persistence.mapper.ChatRoomPersistenceMapper;
import com.picus.core.chat.domain.model.ChatParticipant;
import com.picus.core.chat.domain.model.ChatRoom;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@Import({
        ChatRoomPersistenceAdapter.class,
        ChatRoomPersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatRoomPersistenceAdapterTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;
    @Autowired
    private ChatRoomPersistenceAdapter chatRoomPersistenceAdapter;
    @Autowired
    private ChatParticipantJpaRepository chatParticipantJpaRepository;

    @Test
    @DisplayName("id로 ChatRoom을 조회한다.")
    public void findById() throws Exception {
        // given
        String user1 = "u-1";
        String user2 = "u-2";
        ChatRoomEntity chatRoomEntity = createChatRoomEntity();
        createChatParticipantEntity(chatRoomEntity, user1);
        createChatParticipantEntity(chatRoomEntity, user2);
        clearPersistenceContext();

        // when
        Optional<ChatRoom> optionalChatRoom = chatRoomPersistenceAdapter.findById(chatRoomEntity.getChatRoomNo());

        // then
        assertThat(optionalChatRoom).isPresent();

        ChatRoom chatRoom = optionalChatRoom.get();
        assertThat(chatRoom.getChatParticipants()).hasSize(2)
                .extracting(ChatParticipant::getUserNo)
                .containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    @DisplayName("clientNo와 expertNo로 ChatRoom을 조회한다.")
    public void findByClientNoAndExpertNo() throws Exception {
        // given
        String clientNo = "c-123";
        String expertNo = "e-123";
        ChatRoomEntity chatRoomEntity = createChatRoomEntity();
        ChatParticipantEntity client = createChatParticipantEntity(chatRoomEntity, clientNo);
        ChatParticipantEntity expert = createChatParticipantEntity(chatRoomEntity, expertNo);
        clearPersistenceContext();

        // when
        Optional<ChatRoom> optionalChatRoom = chatRoomPersistenceAdapter.findByClientNoAndExpertNo(clientNo, expertNo);

        // then
        assertThat(optionalChatRoom).isPresent();
        ChatRoom chatRoom = optionalChatRoom.get();
        assertThat(chatRoom.getChatRoomNo()).isNotNull();

        List<ChatParticipant> chatParticipants = chatRoom.getChatParticipants();
        assertThat(chatParticipants).hasSize(2)
                .extracting(
                        ChatParticipant::getChatParticipantNo,
                        ChatParticipant::getUserNo,
                        ChatParticipant::getIsPinned,
                        ChatParticipant::getIsExited
                ).containsExactlyInAnyOrder(
                        tuple(client.getChatParticipantNo(), client.getUserNo(), client.getIsPinned(), client.getIsExited()),
                        tuple(expert.getChatParticipantNo(), expert.getUserNo(), expert.getIsPinned(), expert.getIsExited())
                );
    }

    @Test
    @DisplayName("ChatRoom을 저장한다.")
    public void create() throws Exception {
        // given
        String clientNo = "c-1";
        String expertNo = "e-1";
        ChatParticipant client = createChatParticipant(clientNo);
        ChatParticipant expert = createChatParticipant(expertNo);

        // when
        ChatRoom createChatRoom = chatRoomPersistenceAdapter.create(List.of(client, expert));
        clearPersistenceContext();

        // then - ChatRoomEntity 검증
        Optional<ChatRoomEntity> optional = chatRoomJpaRepository.findById(createChatRoom.getChatRoomNo());
        assertThat(optional).isPresent();
        ChatRoomEntity chatRoomEntity = optional.get();
        assertThat(createChatRoom.getChatRoomNo()).isEqualTo(chatRoomEntity.getChatRoomNo());

        // then - ChatParticipantEntity 검증
        List<String> cpNoList = createChatRoom.getChatParticipants().stream()
                .map(ChatParticipant::getChatParticipantNo)
                .toList();
        List<ChatParticipantEntity> chatParticipantEntities = chatParticipantJpaRepository.findAllById(cpNoList);
        assertThat(chatParticipantEntities).hasSize(2)
                .extracting(ChatParticipantEntity::getUserNo)
                .containsExactlyInAnyOrder(clientNo, expertNo);
    }

    @Test
    @DisplayName("ChatParticipant를 업데이트한다.")
    public void updateChatParticipant() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        String clientNo = "c-123";
        String expertNo = "e-123";
        ChatRoomEntity chatRoomEntity = createChatRoomEntity();
        ChatParticipantEntity shdUptCpE = createChatParticipantEntity(chatRoomEntity, clientNo, false, false);
        createChatParticipantEntity(chatRoomEntity, expertNo);
        clearPersistenceContext();

        // given - 파라미터 셋팅
        LocalDateTime newExitedAt = LocalDateTime.of(2000, 1, 1, 1, 1);
        ChatParticipant uptCp = ChatParticipant.builder()
                .chatParticipantNo(shdUptCpE.getChatParticipantNo())
                .userNo(shdUptCpE.getUserNo())
                .isPinned(true)
                .isExited(true)
                .exitedAt(newExitedAt)
                .build();

        // when
        chatRoomPersistenceAdapter.updateChatParticipant(uptCp);
        clearPersistenceContext();

        // then
        ChatParticipantEntity updated = chatParticipantJpaRepository.findById(shdUptCpE.getChatParticipantNo())
                .orElseThrow();

        assertThat(updated.getIsPinned()).isTrue();
        assertThat(updated.getIsExited()).isTrue();
        assertThat(updated.getExitedAt()).isEqualTo(newExitedAt);
    }

    @Test
    @DisplayName("ChatRoom을 삭제한다.")
    public void delete() throws Exception {
        // given - 데이터베이스에 데이터 셋팅
        String clientNo = "c-123";
        String expertNo = "e-123";
        ChatRoomEntity chatRoomEntity = createChatRoomEntity();
        createChatParticipantEntity(chatRoomEntity, clientNo);
        createChatParticipantEntity(chatRoomEntity, expertNo);
        clearPersistenceContext();

        // when
        chatRoomPersistenceAdapter.delete(chatRoomEntity.getChatRoomNo());

        // then
        assertThat(chatRoomJpaRepository.findAll()).isEmpty();
        assertThat(chatParticipantJpaRepository.findAll()).isEmpty();
    }

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

    private ChatRoomEntity createChatRoomEntity() {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder().build();
        return chatRoomJpaRepository.save(chatRoomEntity);
    }

    private ChatParticipantEntity createChatParticipantEntity(ChatRoomEntity chatRoomEntity, String userNo) {
        ChatParticipantEntity participantEntity = ChatParticipantEntity.builder()
                .chatRoomEntity(chatRoomEntity)
                .userNo(userNo)
                .isPinned(false)
                .isExited(false)
                .build();
        return chatParticipantJpaRepository.save(participantEntity);
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

    private ChatParticipant createChatParticipant(String userNo) {
        return ChatParticipant.builder()
                .userNo(userNo)
                .isPinned(false)
                .isExited(false)
                .build();
    }

}