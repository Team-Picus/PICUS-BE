package com.picus.core.chat.adapter.out.persistence.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "message_files")
public class MessageFileEntity {

    @Id @Tsid
    private String messageFileNo;

    private String fileKey;

    private String extension;
}
