package com.picus.core.chat.config;

import com.picus.core.infrastructure.stomp.resolver.StompCurrentUserArgumentResolver;
import com.picus.core.infrastructure.web.resolver.CurrentUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RabbitMqConfigProperties rabbitMqConfigProperties;
    private final StompHandler stompHandler;

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
        registry.enableStompBrokerRelay("/topic", "/queue") // Rabbit MQ 자체적으로 /ws 같은 접두어를 붙일 수 없도록 되어 있음
                .setRelayHost(rabbitMqConfigProperties.getHost())
                .setRelayPort(rabbitMqConfigProperties.getRelay().getPort())
                .setVirtualHost(rabbitMqConfigProperties.getVirtualHost())
                .setClientLogin(rabbitMqConfigProperties.getRelay().getClientLogin())
                .setClientPasscode(rabbitMqConfigProperties.getRelay().getClientPasscode())
                .setSystemLogin(rabbitMqConfigProperties.getRelay().getSystemLogin())
                .setSystemPasscode(rabbitMqConfigProperties.getRelay().getSystemPasscode());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new StompCurrentUserArgumentResolver()); // @CurrentUser 어노테이션 리졸버 등록
    }
}
