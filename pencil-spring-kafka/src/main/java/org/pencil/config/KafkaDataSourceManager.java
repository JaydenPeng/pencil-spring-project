package org.pencil.config;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pencil
 * @Date 24/08/17
 */
public class KafkaDataSourceManager {

    private final Map<String, KafkaTemplate<String, String>> kafkaTemplates = new HashMap<>();

    public KafkaDataSourceManager(Map<String, KafkaTemplate<String, String>> kafkaTemplates) {
        this.kafkaTemplates.putAll(kafkaTemplates);
    }

    public KafkaTemplate<String, String> getKafkaTemplate(String dataSourceName) {
        return kafkaTemplates.get(dataSourceName);
    }

}
