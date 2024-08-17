package org.pencil;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pencil.anno.KafkaDataSource;
import org.pencil.config.KafkaEnableConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author pencil
 * @Date 24/08/17
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = KafkaEnableConfig.class)
@EmbeddedKafka(partitions = 1, topics = { "testTopic" })
public class MultiKafkaTest {

    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    public MultiKafkaTest(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Test
    @KafkaDataSource("source1")
    public void testSendMsg() {
        // 发送消息到嵌入式Kafka
        kafkaTemplate.send("testTopic", "Hello Kafka!");

        // 配置 Kafka 消费者属性
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 创建 Kafka 消费者
        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer());
        Consumer<String, String> consumer = consumerFactory.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "testTopic");

        // 获取单条记录
        ConsumerRecord<String, String> received = KafkaTestUtils.getSingleRecord(consumer, "testTopic");

        // 验证接收到的消息
        assertThat(received.value()).isEqualTo("Hello Kafka!");

        consumer.close();
    }

}
