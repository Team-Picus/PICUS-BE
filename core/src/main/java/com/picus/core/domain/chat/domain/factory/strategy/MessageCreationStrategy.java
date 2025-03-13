package com.picus.core.domain.chat.domain.factory.strategy;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.dto.response.MessageRes;
import com.picus.core.domain.chat.domain.entity.message.Message;

public interface MessageCreationStrategy {
    Message toEntity(Long roomNo, Long senderNo, SendMsgReq request);
    MessageRes toDto(Message message, Integer unreadCnt);
}
