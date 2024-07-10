package org.pencil.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pencil
 * @Date 24/07/10
 */
@Configuration
public class MetricsConfig {

    @Bean
    public Counter requestCounter(CollectorRegistry registry) {
        return Counter.build()
                .name("request_counter")
                .labelNames("path", "status")
                .help("Total number of requests")
                .register(registry);
    }

}
