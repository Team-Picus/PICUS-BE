package com.picus.core.chat.adapter.out.persistence.jpa.repository;

import com.picus.core.chat.adapter.out.persistence.jpa.entity.MessageReadStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReadStatusJpaRepository extends JpaRepository<MessageReadStatusEntity, String> {
}
