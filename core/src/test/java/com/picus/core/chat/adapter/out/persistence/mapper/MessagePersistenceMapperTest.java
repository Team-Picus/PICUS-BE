package com.picus.core.chat.adapter.out.persistence.mapper;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.entity.MessageReadStatusEntity;
import com.picus.core.chat.adapter.out.persistence.mongo.document.MessageDocument;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.chat.domain.model.MessageReadStatus;
import com.picus.core.chat.domain.model.vo.FileContent;
import com.picus.core.chat.domain.model.vo.MessageType;
import com.picus.core.chat.domain.model.vo.TextContent;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class MessagePersistenceMapperTest {

    private final MessagePersistenceMapper messagePersistenceMapper = new MessagePersistenceMapper();

    @Test
    @DisplayName("Message -> MessageDocument")
    public void toMessageDocument() throws Exception {
        // given
        Message message = Message.builder()
                .chatRoomNo("cr-123")
                .senderNo("u-123")
                .messageType(MessageType.FILE)
                .content(FileContent.builder().fileKey("a.pdf").build())
                .build();

        // when
        MessageDocument messageDocument = messagePersistenceMapper.toMessageDocument(message);

        // then
        assertThat(messageDocument.getChatRoomNo()).isEqualTo(message.getChatRoomNo());
        assertThat(messageDocument.getSenderNo()).isEqualTo(message.getSenderNo());
        assertThat(messageDocument.getContent()).isEqualTo(message.getContent());
        assertThat(messageDocument.getMessageType()).isEqualTo(message.getMessageType());
    }

    @Test
    @DisplayName("Domain -> Entity")
    public void toEntity() throws Exception {
        // given
        MessageReadStatus domain = MessageReadStatus.builder()
                .userNo("u-123")
                .isRead(true)
                .build();

        // when
        MessageReadStatusEntity entity = messagePersistenceMapper.toMessageReadStatusEntity(domain);

        // then
        assertThat(entity.getUserNo()).isEqualTo(domain.getUserNo());
        assertThat(entity.getIsRead()).isEqualTo(domain.getIsRead());
    }

    @Test
    @DisplayName("Entity -> Domain")
    public void toMessageDomain() throws Exception {
        // given
        ObjectId oid = new ObjectId("64f9a3f1e6b54d7d8c1e1234");

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .chatRoomNo("cr-123")
                .build();
        MessageDocument messageDocument = MessageDocument.builder()
                .messageNo(oid)
                .chatRoomNo(chatRoomEntity.getChatRoomNo())
                .senderNo("u-123")
                .content(TextContent.builder().text("content").build())
                .messageType(MessageType.TEXT)
                .build();

        MessageReadStatusEntity readStatus1 =
                createMessageReadStatusEntity("mr-1", "u-123", true, chatRoomEntity.getChatRoomNo());
        MessageReadStatusEntity readStatus2 =
                createMessageReadStatusEntity("mr-2", "u-345", false, chatRoomEntity.getChatRoomNo());

        // when
        Message messageDomain = messagePersistenceMapper.toMessageDomain(messageDocument, List.of(readStatus1, readStatus2));

        // then
        assertThat(messageDomain.getMessageNo()).isEqualTo(oid.toHexString());
        assertThat(messageDomain.getChatRoomNo()).isEqualTo("cr-123");
        assertThat(messageDomain.getSenderNo()).isEqualTo("u-123");
        assertThat(messageDomain.getContent()).isInstanceOf(TextContent.class);
        assertThat(((TextContent) messageDomain.getContent()).text()).isEqualTo("content");
        assertThat(messageDomain.getMessageType()).isEqualTo(MessageType.TEXT);

        assertThat(messageDomain.getMessageReadStatuses())
                .hasSize(2)
                .extracting("userNo", "isRead")
                .containsExactlyInAnyOrder(
                        tuple("u-123", true),
                        tuple("u-345", false)
                );
    }

    private MessageReadStatusEntity createMessageReadStatusEntity(String messageReadStatusNo, String userNo, boolean isRead, String chatRoomNo) {
        return MessageReadStatusEntity.builder()
                .messageReadStatusNo(messageReadStatusNo)
                .userNo(userNo)
                .chatRoomNo(chatRoomNo)
                .isRead(isRead)
                .build();
    }

}