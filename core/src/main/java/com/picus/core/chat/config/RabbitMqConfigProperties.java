package com.picus.core.chat.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@ConfigurationProperties("rabbitmq")
public class RabbitMqConfigProperties {

    private final String host;
    private final String virtualHost;
    private final Relay relay;

    public RabbitMqConfigProperties(String host, String virtualHost, @DefaultValue Relay relay) {
        this.host = host;
        this.virtualHost = virtualHost;
        this.relay = relay;
    }

    @Getter
    @AllArgsConstructor
    public static class Relay {
        private int port;
        private String clientLogin;
        private String clientPasscode;
        private String systemLogin;
        private String systemPasscode;
    }
}
