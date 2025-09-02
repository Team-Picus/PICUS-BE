package com.picus.core.chat.adapter.in;

import com.picus.core.chat.adapter.in.web.data.request.SendMessageRequest;
import com.picus.core.shared.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SendMessageController {
    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/{roomId}")
    public void send(@DestinationVariable Long roomId, @RequestBody @Valid SendMessageRequest request, @CurrentUser String userNo) {

    }
}
