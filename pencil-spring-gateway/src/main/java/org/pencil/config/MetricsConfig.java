package org.pencil.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pencil
 * @Date 24/08/10
 */
@Configuration
public class MetricsConfig {


    @Bean
    public Summary responseTimeSummary(CollectorRegistry registry) {
        return Summary.build()
                .name("gateway_response_time_seconds")
                .labelNames("server")
                .help("Response time of gateway in seconds")
                // p50 50th percentile (p50) with 5% error
                .quantile(0.5, 0.05)
                // p95 95th percentile (p95) with 1% error
                .quantile(0.95, 0.01)
                // p99 99th percentile (p99) with 0.5% error
                .quantile(0.99, 0.005)
                .register(registry);
    }

    @Bean
    public Counter requestCounter(CollectorRegistry registry) {
        return Counter.build()
                .name("gateway_request_total")
                .help("Total number of requests")
                .labelNames("server", "status")
                .register(registry);
    }


}
