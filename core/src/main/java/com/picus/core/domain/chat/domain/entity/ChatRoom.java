package com.picus.core.domain.chat.domain.entity;

import com.picus.core.global.common.base.BaseEntity;
import com.picus.core.global.common.converter.LongSetConverter;
import com.picus.core.global.common.exception.RestApiException;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._BAD_REQUEST;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id @Tsid
    @Column(name = "room_no")
    private Long id;

    @Convert(converter = LongSetConverter.class)
    private Set<Long> chatUsers = new HashSet<>();

    private LocalDateTime lastMessageAt;

    private String thumbnailMessage;

    public void updateLastMessage(String thumbnailMessage) {
        this.lastMessageAt = LocalDateTime.now();
        this.thumbnailMessage = thumbnailMessage;
    }
}
