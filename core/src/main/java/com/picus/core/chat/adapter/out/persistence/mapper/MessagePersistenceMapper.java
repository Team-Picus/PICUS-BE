package com.picus.core.chat.adapter.out.persistence.mapper;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.MessageReadStatusEntity;
import com.picus.core.chat.adapter.out.persistence.mongo.document.MessageDocument;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.chat.domain.model.MessageReadStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessagePersistenceMapper {
    public Message toMessageDomain(MessageDocument messageDocument, List<MessageReadStatusEntity> readStatusEntities) {
        return Message.builder()
                .messageNo(messageDocument.getMessageNo().toHexString())
                .chatRoomNo(messageDocument.getChatRoomNo())
                .senderNo(messageDocument.getSenderNo())
                .content(messageDocument.getContent())
                .messageType(messageDocument.getMessageType())
                .messageReadStatuses(
                        readStatusEntities.stream()
                                .map(this::toMessageReadStatusDomain)
                                .toList()
                )
                .createdAt(messageDocument.getCreatedAt())
                .updatedAt(messageDocument.getUpdatedAt())
                .deletedAt(messageDocument.getDeletedAt())
                .build();
    }

    private MessageReadStatus toMessageReadStatusDomain(MessageReadStatusEntity readStatusEntity) {
        return MessageReadStatus.builder()
                .messageReadStatusNo(readStatusEntity.getMessageReadStatusNo())
                .chatRoomNo(readStatusEntity.getChatRoomNo())
                .userNo(readStatusEntity.getUserNo())
                .isRead(readStatusEntity.getIsRead())
                .build();
    }

    public MessageDocument toMessageDocument(Message message) {
        return MessageDocument.builder()
                .chatRoomNo(message.getChatRoomNo())
                .senderNo(message.getSenderNo())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .build();
    }

    public MessageReadStatusEntity toMessageReadStatusEntity(MessageReadStatus messageReadStatus) {
        return MessageReadStatusEntity.builder()
                .userNo(messageReadStatus.getUserNo())
                .chatRoomNo(messageReadStatus.getChatRoomNo())
                .isRead(messageReadStatus.getIsRead())
                .build();
    }
}
