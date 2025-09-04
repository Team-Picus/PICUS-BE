package com.picus.core.chat.adapter.in.web.data.response;

import com.picus.core.chat.domain.model.vo.MessageType;

public sealed interface CreateMessageResponse
        permits CreateTextMessageResponse, CreateImageMessageResponse, CreateFileMessageResponse, CreateReservationMessageResponse {
    MessageType type();
}
