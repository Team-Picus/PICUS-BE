package com.picus.core.chat.adapter.out.persistence.mongo.repository;

import com.picus.core.chat.adapter.out.persistence.mongo.document.MessageDocument;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;
import java.util.List;

public interface MessageMongoRepository extends MongoRepository<MessageDocument, ObjectId> {
    // createdAt 기준 마지막 1건
    Optional<MessageDocument> findFirstByChatRoomNoOrderByCreatedAtDesc(String chatRoomNo);

    @Query(value = "{ 'chatRoomNo': ?0, 'content.text': { $regex: ?1, $options: 'i' } }")
    List<MessageDocument> findByChatRoomNoAndContentTextRegex(String chatRoomNo, String regex, Pageable pageable);
}
