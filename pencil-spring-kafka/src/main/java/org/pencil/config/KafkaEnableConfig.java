package org.pencil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

/**
 * kafka启动配置
 * @author pencil
 * @Date 24/08/17
 */
public class KafkaEnableConfig {


    @Bean
    public KafkaDynamicConfig kafkaDynamicConfig() {
        return new KafkaDynamicConfig();
    }

    @Bean
    public KafkaDataSourceManager kafkaDataSourceManager(Map<String, KafkaTemplate<String, String>> kafkaTemplates) {
        return new KafkaDataSourceManager(kafkaTemplates);
    }

}
