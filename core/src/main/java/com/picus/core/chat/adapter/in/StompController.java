package com.picus.core.chat.adapter.in;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/{roomId}/test")
    public void test(@DestinationVariable Long roomId, String testMessage) {
        log.info("STOMP 연결 테스트) 방{}, 테스트 메시지:{}", roomId, testMessage);
        messageTemplate.convertAndSend("/topic/" + roomId, testMessage);
    }
}
