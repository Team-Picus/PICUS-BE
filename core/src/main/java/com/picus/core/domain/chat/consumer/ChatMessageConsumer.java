package com.picus.core.domain.chat.consumer;

import com.picus.core.domain.chat.event.ChatMessageEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageConsumer {

    // chat.queue에서 메시지 수신
    @RabbitListener(queues = "#{chatQueue.name}")
    public void receiveMessage(ChatMessageEvent messageEvent) {
        // 수신된 메시지 처리 (예: WebSocket으로 실시간 전송)
        System.out.println("수신된 메시지: " + messageEvent.getContent());
        // 추가 처리 로직(예: 메시지 저장, 읽음 상태 업데이트 등)을 여기에 구현
    }
}
