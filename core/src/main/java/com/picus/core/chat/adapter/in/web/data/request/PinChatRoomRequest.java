package com.picus.core.chat.adapter.in.web.data.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record PinChatRoomRequest(
        @NotNull @Size(min = 1) List<String> chatRoomNos
) {
}
