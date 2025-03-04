package com.picus.core.domain.chat.ui;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.application.usecase.SendMessageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SendMessageUseCase sendMessageUseCase;

    @MessageMapping("chat.message.{roomNo}")
    public void sendMessage(@DestinationVariable Long roomNo, SendMsgReq message) {

    }
}
