package com.picus.core;

import com.picus.core.infrastructure.security.CorsProperties;
import com.picus.core.user.config.TokenPrefixProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties({
		CorsProperties.class,
		TokenPrefixProperties.class
})
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
