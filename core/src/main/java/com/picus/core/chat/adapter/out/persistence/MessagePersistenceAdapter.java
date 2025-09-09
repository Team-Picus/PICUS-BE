package com.picus.core.chat.adapter.out.persistence;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.MessageReadStatusEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.MessageReadStatusJpaRepository;
import com.picus.core.chat.adapter.out.persistence.mapper.MessagePersistenceMapper;
import com.picus.core.chat.adapter.out.persistence.mongo.document.MessageDocument;
import com.picus.core.chat.adapter.out.persistence.mongo.repository.MessageMongoRepository;
import com.picus.core.chat.application.port.out.MessageCreatePort;
import com.picus.core.chat.application.port.out.MessageReadPort;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@PersistenceAdapter
@RequiredArgsConstructor
public class MessagePersistenceAdapter implements MessageCreatePort, MessageReadPort {

    private final MessageMongoRepository messageMongoRepository;
    private final MessageReadStatusJpaRepository messageReadStatusJpaRepository;

    private final MessagePersistenceMapper messageMapper;

    @Override
    public Message create(Message message) {
        // TODO:  JPA(RDB) + MongoDB 트랜잭션 처리
        // Message Domain -> Message Document
        MessageDocument messageDocument = messageMapper.toMessageDocument(message);
        // Message Document 저장
        MessageDocument savedMessageDocument = messageMongoRepository.save(messageDocument);

        // MessageReadStatus Domain -> MessageReadStatus Entity

        List<MessageReadStatusEntity> readStatusEntities = message.getMessageReadStatuses().stream()
                .map(readStatus -> {
                    MessageReadStatusEntity readStatusEntity = messageMapper.toMessageReadStatusEntity(readStatus);
                    readStatusEntity.bindMessageNo(savedMessageDocument.getMessageNo().toHexString());
                    return readStatusEntity;
                })
                .toList();
        // MessageReadStatus Entity 저장
        List<MessageReadStatusEntity> savedReadStatusEntities = messageReadStatusJpaRepository.saveAll(readStatusEntities);// TODO: bulk insert

        // Domain 변환하여 리턴
        return messageMapper.toMessageDomain(savedMessageDocument, savedReadStatusEntities);
    }

    @Override
    public Optional<Message> findLastByChatRoomNo(String chatRoomNo) {
        // 현재 채팅방의 가장 마지막 메시지 조회
        Optional<MessageDocument> optionalLastMessageDoc = messageMongoRepository.findFirstByChatRoomNoOrderByCreatedAtDesc(chatRoomNo);
        if(optionalLastMessageDoc.isEmpty())
            return Optional.empty();

        MessageDocument messageDocument = optionalLastMessageDoc.get();
        List<MessageReadStatusEntity> readStatusEntities =
                messageReadStatusJpaRepository.findByMessageNo(messageDocument.getMessageNo().toHexString());

        return Optional.of(messageMapper.toMessageDomain(messageDocument, readStatusEntities));
    }

    @Override
    public Optional<Message> findLastByChatRoomNoAndKeywordContaining(String chatRoomNo, String keyword) {
        // 키워드를 안전하게 이스케이프하여 부분일치 정규식 생성 (대소문자 무시)
        String escaped = Pattern.quote(keyword == null ? "" : keyword.trim());
        String regex = ".*" + escaped + ".*";

        // 해당 채팅방의 해당 키워드가 포함된 메시지 조회
        PageRequest page = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<MessageDocument> messageDocuments =
                messageMongoRepository.findByChatRoomNoAndContentTextRegex(chatRoomNo, regex, page);

        Optional<MessageDocument> optionalMessage = messageDocuments.isEmpty() ? Optional.empty() : Optional.of(messageDocuments.getFirst());

        if (optionalMessage.isEmpty()) return Optional.empty();

        MessageDocument messageDocument = optionalMessage.get();
        List<MessageReadStatusEntity> readStatusEntities =
                messageReadStatusJpaRepository.findByMessageNo(messageDocument.getMessageNo().toHexString());

        return Optional.of(messageMapper.toMessageDomain(messageDocument, readStatusEntities));
    }

    @Override
    public long countUnReadMessageByChatRoomNoAndUserNo(String chatRoomNo, String userNo) {
        return messageReadStatusJpaRepository
                .countByChatRoomNoAndUserNoAndIsReadFalse(chatRoomNo, userNo);
    }
}
