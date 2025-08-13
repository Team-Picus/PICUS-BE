package com.picus.core.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RabbitMqConfigProperties rabbitMqConfigProperties;

    @Value("${cors.allowed-origins}")
    private String corsAllowedOrigins;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/connect")
                .setAllowedOriginPatterns(corsAllowedOrigins.split(","))
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/ws/publish");

        // RabbitMQ 메시지 브로커 사용
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(rabbitMqConfigProperties.getHost())
                .setRelayPort(rabbitMqConfigProperties.getRelay().getPort())
                .setVirtualHost(rabbitMqConfigProperties.getVirtualHost())
                .setClientLogin(rabbitMqConfigProperties.getRelay().getClientLogin())
                .setClientPasscode(rabbitMqConfigProperties.getRelay().getClientPasscode())
                .setSystemLogin(rabbitMqConfigProperties.getRelay().getSystemLogin())
                .setSystemPasscode(rabbitMqConfigProperties.getRelay().getSystemPasscode());
    }
}
