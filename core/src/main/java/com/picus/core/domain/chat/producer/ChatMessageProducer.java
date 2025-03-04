package com.picus.core.domain.chat.producer;

import com.picus.core.domain.chat.event.ChatMessageEvent;
import com.picus.core.global.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public ChatMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(ChatMessageEvent messageEvent) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_ROUTING_KEY, messageEvent);
    }
}
