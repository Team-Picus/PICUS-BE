package com.picus.core.chat.adapter.out.persistence.mongo.document;

import com.picus.core.chat.domain.model.vo.MessageContent;
import com.picus.core.chat.domain.model.vo.MessageType;
import com.picus.core.shared.common.BaseDocument;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Getter
@Builder
public class MessageDocument extends BaseDocument {

    @Id
    private ObjectId messageNo; // MongoDB _id

    private String chatRoomNo;
    private String senderNo;
    private MessageContent content;
    private MessageType messageType;
}
