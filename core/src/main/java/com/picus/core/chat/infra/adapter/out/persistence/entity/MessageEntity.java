package com.picus.core.chat.infra.adapter.out.persistence.entity;

import com.picus.core.chat.domain.model.vo.MessageType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id @Tsid
    private String messageNo;

    private String content;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    private Boolean isRead;
}
