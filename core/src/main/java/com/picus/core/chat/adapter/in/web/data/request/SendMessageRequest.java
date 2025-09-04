package com.picus.core.chat.adapter.in.web.data.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.picus.core.chat.domain.model.vo.MessageType;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"              // JSON에 들어오는 구분자 필드명
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SendTextMessageRequest.class,  name = "TEXT"),
        @JsonSubTypes.Type(value = SendImageMessageRequest.class, name = "IMAGE"),
        @JsonSubTypes.Type(value = SendFileMessageRequest.class,  name = "FILE"),
        @JsonSubTypes.Type(value = SendReservationMessageRequest.class, name = "RESERVATION")
})
public sealed interface SendMessageRequest
        permits SendTextMessageRequest, SendImageMessageRequest, SendFileMessageRequest, SendReservationMessageRequest  {
    MessageType type();
}
