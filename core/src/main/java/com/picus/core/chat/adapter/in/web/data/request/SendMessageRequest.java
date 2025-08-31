package com.picus.core.chat.adapter.in.web.data.request;

import com.picus.core.chat.domain.model.vo.MessageType;

public sealed interface SendMessageRequest
        permits SendTextMessageRequest, SendImageMessageRequest, SendFileMessageRequest, SendReservationMessageRequest  {
    MessageType type();
}
