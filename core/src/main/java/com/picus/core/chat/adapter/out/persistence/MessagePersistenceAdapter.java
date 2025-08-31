package com.picus.core.chat.adapter.out.persistence;

import com.picus.core.chat.application.port.out.MessageCreatePort;
import com.picus.core.chat.domain.model.Message;
import com.picus.core.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class MessagePersistenceAdapter implements MessageCreatePort {

    @Override
    public Message create(Message message) {
        return null;
    }
}
