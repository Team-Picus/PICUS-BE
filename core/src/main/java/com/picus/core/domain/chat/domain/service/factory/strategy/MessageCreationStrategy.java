package com.picus.core.domain.chat.domain.service.factory.strategy;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.domain.entity.message.Message;

public interface MessageCreationStrategy {
    Message create(Long roomNo, Long senderNo, SendMsgReq request);
}
