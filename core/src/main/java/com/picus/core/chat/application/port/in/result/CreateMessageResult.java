package com.picus.core.chat.application.port.in.result;

import com.picus.core.chat.domain.model.vo.MessageType;

public sealed interface CreateMessageResult
        permits CreateTextMessageResult, CreateImageMessageResult, CreateFileMessageResult, CreateReservationMessageResult {
    MessageType type();
}
