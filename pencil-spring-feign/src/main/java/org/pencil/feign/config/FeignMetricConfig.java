package org.pencil.feign.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.springframework.context.annotation.Bean;

/**
 * @author pencil
 * @Date 24/07/18
 */
public class FeignMetricConfig {

    public static final String FEIGN_REQUEST_COUNTER = "feign_request_counter";

    public static final String PATH = "path";

    public static final String STATUS = "status";


    @Bean
    public Counter feignCounter(CollectorRegistry registry) {
        return Counter.build()
                .name(FEIGN_REQUEST_COUNTER)
                .labelNames(PATH, STATUS)
                .help("feign request counter")
                .register(registry);
    }

}
