package org.pencil.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author pencil
 * @Date 24/08/08
 */
@Configuration
public class GatewayConfig {

    @Bean
    @ConditionalOnMissingBean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
