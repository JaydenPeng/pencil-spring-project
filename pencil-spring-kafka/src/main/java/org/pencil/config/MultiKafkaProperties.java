package org.pencil.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author pencil
 * @Date 24/08/17
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "pencil.kafka")
public class MultiKafkaProperties {

    private Map<String, KafkaProperties> dataSources;

}
