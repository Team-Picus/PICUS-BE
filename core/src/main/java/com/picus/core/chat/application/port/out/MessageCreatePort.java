package com.picus.core.chat.application.port.out;

import com.picus.core.chat.domain.model.Message;

public interface MessageCreatePort {

    Message create(Message message);
}
