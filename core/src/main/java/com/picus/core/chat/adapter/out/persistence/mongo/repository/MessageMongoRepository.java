package com.picus.core.chat.adapter.out.persistence.mongo.repository;

import com.picus.core.chat.adapter.out.persistence.mongo.document.MessageDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MessageMongoRepository extends MongoRepository<MessageDocument, ObjectId> {
    // createdAt 기준 마지막 1건
    Optional<MessageDocument> findFirstByChatRoomNoOrderByCreatedAtDesc(String chatRoomNo);
}
