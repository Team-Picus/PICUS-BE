package com.picus.core.chat.application.port.in;

import com.picus.core.chat.application.port.in.command.CreateMessageCommand;
import com.picus.core.chat.application.port.in.result.CreateMessageResult;

public interface CreateMessageUseCase {
    CreateMessageResult create(CreateMessageCommand command);
}
