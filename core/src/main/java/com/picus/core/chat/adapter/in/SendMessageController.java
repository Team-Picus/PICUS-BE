package com.picus.core.chat.adapter.in;

import com.picus.core.chat.adapter.in.web.data.request.*;
import com.picus.core.chat.adapter.in.web.mapper.SendMessageWebMapper;
import com.picus.core.chat.application.port.in.CreateMessageUseCase;
import com.picus.core.chat.application.port.in.command.CreateMessageCommand;
import com.picus.core.chat.application.port.in.result.CreateMessageResult;
import com.picus.core.shared.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
@RequiredArgsConstructor
public class SendMessageController {
    private final SimpMessageSendingOperations messageTemplate;
    private final CreateMessageUseCase useCase;
    private final SendMessageWebMapper webMapper;

    @MessageMapping("/{chatRoomNo}")
    public void send(@DestinationVariable String chatRoomNo, @Payload @Valid SendMessageRequest request, @CurrentUser String userNo) {
        CreateMessageCommand command = webMapper.toCommand(request, chatRoomNo, userNo);
        CreateMessageResult createMessageResult = useCase.create(command); // 메시지 저장
        messageTemplate.convertAndSend("/topic/" + chatRoomNo, webMapper.toResponse(createMessageResult));
    }
}
