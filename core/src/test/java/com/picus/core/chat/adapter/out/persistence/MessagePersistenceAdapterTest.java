package com.picus.core.chat.adapter.out.persistence;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.MessageReadStatusEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.MessageReadStatusJpaRepository;
import com.picus.core.chat.adapter.out.persistence.mapper.MessagePersistenceMapper;
import com.picus.core.chat.adapter.out.persistence.mongo.document.MessageDocument;
import com.picus.core.chat.adapter.out.persistence.mongo.repository.MessageMongoRepository;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.chat.domain.model.MessageReadStatus;
import com.picus.core.chat.domain.model.vo.FileContent;
import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;
import com.picus.core.chat.domain.model.vo.TextContent;
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

    @Test
    @DisplayName("특정 키워드가 포함된 최신 메시지를 조회한다 - 정확 일치")
    public void findLastByKeyword_exactMatch() {
        String chatRoomNo = "cr-1";
        createTextMessageDocument(chatRoomNo, "u-1", "u-2", "회의 준비 완료");
        createTextMessageDocument(chatRoomNo, "u-1", "u-2", "회의 시작합니다"); // 최신
        clearPersistenceContext();

        Optional<Message> result = messagePersistenceAdapter.findLastByChatRoomNoAndKeywordContaining(chatRoomNo, "회의");

        assertThat(result).isPresent();
        assertThat(((TextContent) result.get().getContent()).text()).isEqualTo("회의 시작합니다");
    }

    @Test
    @DisplayName("특정 키워드가 포함된 최신 메시지를 조회한다 - 대소문자 무시")
    public void findLastByKeyword_ignoreCase() {
        String chatRoomNo = "cr-1";
        createTextMessageDocument(chatRoomNo, "u-1", "u-2", "Let's start the MEETING now");
        clearPersistenceContext();

        Optional<Message> result = messagePersistenceAdapter.findLastByChatRoomNoAndKeywordContaining(chatRoomNo, "meeting");

        assertThat(result).isPresent();
        assertThat(((TextContent) result.get().getContent()).text()).isEqualTo("Let's start the MEETING now");
    }

    @Test
    @DisplayName("특정 키워드가 포함된 최신 메시지를 조회한다 - 부분 일치")
    public void findLastByKeyword_partialMatch() {
        String chatRoomNo = "cr-1";
        createTextMessageDocument(chatRoomNo, "u-1", "u-2", "긴급공지: 서버 다운됨");
        clearPersistenceContext();

        Optional<Message> result = messagePersistenceAdapter.findLastByChatRoomNoAndKeywordContaining(chatRoomNo, "급공");

        assertThat(result).isPresent();
        assertThat(((TextContent) result.get().getContent()).text()).isEqualTo("긴급공지: 서버 다운됨");
    }

    @Test
    @DisplayName("특정 키워드가 포함된 최신 메시지를 조회한다 - 불일치시 결과 없음")
    public void findLastByKeyword_noMatch() {
        String chatRoomNo = "cr-1";
        createTextMessageDocument(chatRoomNo, "u-1", "u-2", "점심 먹었어?");
        clearPersistenceContext();

        Optional<Message> result = messagePersistenceAdapter.findLastByChatRoomNoAndKeywordContaining(chatRoomNo, "회의");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("특정 키워드가 포함된 최신 메시지를 조회한다 - 다른 채팅방은 섞이지 않음")
    public void findLastByKeyword_differentChatRooms() {
        String chatRoomNo1 = "cr-1";
        String chatRoomNo2 = "cr-2";

        createTextMessageDocument(chatRoomNo1, "u-1", "u-2", "회의 시작합니다");
        createTextMessageDocument(chatRoomNo2, "u-3", "u-4", "회의는 내일 진행합니다.");
        clearPersistenceContext();

        Optional<Message> result1 = messagePersistenceAdapter.findLastByChatRoomNoAndKeywordContaining(chatRoomNo1, "회의");
        Optional<Message> result2 = messagePersistenceAdapter.findLastByChatRoomNoAndKeywordContaining(chatRoomNo2, "회의");

        assertThat(result1).isPresent();
        assertThat(((TextContent) result1.get().getContent()).text()).isEqualTo("회의 시작합니다");

        assertThat(result2).isPresent();
        assertThat(((TextContent) result2.get().getContent()).text()).isEqualTo("회의는 내일 진행합니다.");
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

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}