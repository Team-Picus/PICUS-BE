package com.picus.core.chat.adapter.out.persistence;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.MessageReadStatusEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.MessageReadStatusJpaRepository;
import com.picus.core.chat.adapter.out.persistence.mapper.MessagePersistenceMapper;
import com.picus.core.chat.adapter.out.persistence.mongo.repository.MessageMongoRepository;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.chat.domain.model.MessageReadStatus;
import com.picus.core.chat.domain.model.vo.FileContent;
import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;
import jakarta.persistence.EntityManager;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Import({
        MessagePersistenceAdapter.class,
        MessagePersistenceMapper.class
})
@SpringBootTest // MongoDB를 사용하기 때문에 @DataJpaTest 사용불가
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessagePersistenceAdapterTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;
    @Autowired
    private MessagePersistenceAdapter messagePersistenceAdapter;
    @Autowired
    private MessageMongoRepository messageMongoRepository;
    @Autowired
    private MessageReadStatusJpaRepository messageReadStatusJpaRepository;

    @AfterEach
    void tearDown() {
        messageMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Message를 저장한다.")
    public void create() throws Exception {
        // given
        Message message = createMessageDomain("cr-123", "u-123", MessageType.FILE, FileContent.builder().fileKey("a.pdf").build(),
                List.of(createMessageReadStatusDomain("cr-123", "u-123", true),
                        createMessageReadStatusDomain("cr-123", "u-456", false)));
        clearPersistenceContext();

        // when
        Message savedMessage = messagePersistenceAdapter.create(message);

        // then
        // _id(ObjectId) -> String hex 로 매핑되었는지
        assertThat(savedMessage.getMessageNo())
                .isNotBlank()
                .hasSize(24)
                .matches("[0-9a-f]{24}");

        // then - 기본 필드 매핑
        assertThat(savedMessage.getChatRoomNo()).isNotBlank();
        assertThat(savedMessage.getSenderNo()).isEqualTo("u-123");
        assertThat(savedMessage.getMessageType()).isEqualTo(MessageType.FILE);

        // then - 콘텐츠: FileContent로 저장/조회되었는지
        assertThat(savedMessage.getContent()).isInstanceOf(FileContent.class);
        assertThat(((FileContent) savedMessage.getContent()).fileKey()).isEqualTo("a.pdf");

        // then - 읽음 상태 리스트 검증: 사이즈 + 내용 (userNo, isRead)
        assertThat(savedMessage.getMessageReadStatuses())
                .hasSize(2)
                .extracting("userNo", "isRead")
                .containsExactlyInAnyOrder(
                        tuple("u-123", true),
                        tuple("u-456", false)
                );

        // then - 데이터베이스에 저장되었는지 검증
        assertThat(messageMongoRepository.findById(new ObjectId(savedMessage.getMessageNo())))
                .isPresent();

        for (MessageReadStatus messageReadStatus : savedMessage.getMessageReadStatuses()) {
            Optional<MessageReadStatusEntity> optional = messageReadStatusJpaRepository.findById(messageReadStatus.getMessageReadStatusNo());
            assertThat(optional).isPresent();

            MessageReadStatusEntity messageReadStatusEntity = optional.get();
            assertThat(messageReadStatusEntity.getMessageNo()).isEqualTo(savedMessage.getMessageNo());
        }
    }

    private Message createMessageDomain(
            String chatRoomNo, String senderNo, MessageType messageType, MessageContent content,
            List<MessageReadStatus> messageReadStatuses) {
        return Message.builder()
                .chatRoomNo(chatRoomNo)
                .senderNo(senderNo)
                .messageType(messageType)
                .content(content)
                .messageReadStatuses(messageReadStatuses)
                .build();
    }

    private MessageReadStatus createMessageReadStatusDomain(String chatRoomNo, String userNo, boolean isRead) {
        return MessageReadStatus.builder()
                .chatRoomNo(chatRoomNo)
                .userNo(userNo)
                .isRead(isRead)
                .build();
    }

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

    private ChatRoomEntity createChatRoomEntity() {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder().build();
        return chatRoomJpaRepository.save(chatRoomEntity);
    }
}