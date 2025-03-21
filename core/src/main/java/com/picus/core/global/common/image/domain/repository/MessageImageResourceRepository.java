package com.picus.core.global.common.image.domain.repository;

import com.picus.core.domain.chat.domain.entity.message.MessageImageResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageImageResourceRepository extends JpaRepository<MessageImageResource, Long> {
}
