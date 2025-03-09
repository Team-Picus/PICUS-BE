package com.picus.core.domain.chat.domain.repository;

import com.picus.core.domain.chat.domain.entity.message.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, Long> {

    @Query(value = "{ 'roomNo': ?0, 'sendAt': { '$gt': ?1 } }", sort = "{ 'sendAt': 1 }")
    List<Message> findNewMessages(Long roomNo, LocalDateTime lastEntryTime);

    @Query(value = "{ 'roomNo': ?0, 'sendAt': { '$gt': ?1 }, 'isRead': false }")
    List<Message> findUnreadMessages(Long roomNo, LocalDateTime lastEntryTime);

    List<Message> findTop50ByRoomNoAndSendAtLessThanOrderBySendAtAsc(Long roomNo, LocalDateTime sendAt);

}
