package com.picus.core.chat.adapter.out.persistence.mongo.document;

import com.picus.core.chat.domain.model.vo.MessageType;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "messages")
@Getter
public class MessageDocument {

    @Id
    private String messageNo; // MongoDB _id

    private String chatRoomNo;
    private String senderNo;
    private Map<String, Object> content;
    private MessageType messageType;
}
