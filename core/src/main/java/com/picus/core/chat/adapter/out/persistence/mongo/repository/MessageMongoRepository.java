package com.picus.core.chat.adapter.out.persistence.mongo.repository;

import com.picus.core.chat.adapter.out.persistence.mongo.document.MessageDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageMongoRepository extends MongoRepository<MessageDocument, ObjectId> {
}
