package org.pencil.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pencil
 * @Date 24/08/17
 */
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaDynamicConfig {

    /**
     * 生产者多数据源配置
     * @param multiKafkaProperties 多数据源
     * @return map
     */
    @Bean
    public Map<String, KafkaTemplate<String, String>> kafkaTemplates(MultiKafkaProperties multiKafkaProperties) {
        Map<String, KafkaTemplate<String, String>> kafkaTemplates = new HashMap<>();

        multiKafkaProperties.getDataSources().forEach((name, config) -> {
            Map<String, Object> configProps = new HashMap<>(config.buildProducerProperties());

            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getProducer().getKeySerializer());
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getProducer().getValueSerializer());

            ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
            kafkaTemplates.put(name, new KafkaTemplate<>(producerFactory));
        });

        return kafkaTemplates;
    }

    /**
     * 消费者多数据源配置
     * @param multiKafkaProperties 多数据源
     * @return 结果
     */
    @Bean
    public Map<String, ConcurrentKafkaListenerContainerFactory<String, String>> kafkaListenerContainerFactories(MultiKafkaProperties multiKafkaProperties) {
        Map<String, ConcurrentKafkaListenerContainerFactory<String, String>> factories = new HashMap<>();

        multiKafkaProperties.getDataSources().forEach((name, config) -> {

            Map<String, Object> consumerProps = new HashMap<>(config.buildConsumerProperties());
            consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getConsumer().getKeyDeserializer());
            consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getConsumer().getValueDeserializer());

            ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            factories.put(name, factory);
        });

        return factories;
    }

}
