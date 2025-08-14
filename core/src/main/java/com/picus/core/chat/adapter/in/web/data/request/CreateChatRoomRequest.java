package com.picus.core.chat.adapter.in.web.data.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateChatRoomRequest(
    @NotBlank String expertNo
) {}
