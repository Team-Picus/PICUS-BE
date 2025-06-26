package com.picus.core.old.domain.chat.application.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.picus.core.old.domain.chat.domain.entity.message.MessageType;
import lombok.Getter;

@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,              // 이름 기반으로 타입 식별
        include = JsonTypeInfo.As.PROPERTY,        // JSON 프로퍼티로 포함
        property = "messageType",                  // 프로퍼티 이름: messageType (실제 값은 MessageType의 값)
        visible = true                             // 역직렬화 시 messageType 필드도 그대로 유지
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextMessageRes.class, name = "TEXT"),
        @JsonSubTypes.Type(value = SyncRequestRes.class, name = "IMAGE")
})
public abstract class MessageRes {

    private String id;
    private MessageType messageType;

    protected MessageRes(String id, MessageType messageType) {
        this.id = id;
        this.messageType = messageType;
    }
}
