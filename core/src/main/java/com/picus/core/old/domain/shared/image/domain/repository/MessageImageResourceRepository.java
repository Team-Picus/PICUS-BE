package com.picus.core.old.domain.shared.image.domain.repository;

import com.picus.core.old.domain.chat.domain.entity.message.MessageImageResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageImageResourceRepository extends JpaRepository<MessageImageResource, Long> {
}
