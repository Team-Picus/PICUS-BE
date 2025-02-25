package com.picus.core.domain.chat.domain.repository;

import com.picus.core.domain.chat.domain.entity.message.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, Long> {
}
