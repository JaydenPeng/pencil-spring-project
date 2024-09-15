package org.pencil.queue;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Set;

/**
 * 延时队列
 *
 * @author pencil
 * @Date 24 /08/19
 */
@AllArgsConstructor
public class DelayedQueueService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Enqueue.
     *
     * @param queueName    the queue name
     * @param task         the task
     * @param delaySeconds the delay seconds
     */
    public void enqueue(String queueName, String task, long delaySeconds) {
        long timestamp = Instant.now().getEpochSecond() + delaySeconds;
        redisTemplate.opsForZSet().add(queueName, task, timestamp);
    }

    /**
     * 这种方法的一个缺点是消费者需要不断地轮询 Redis 以检查是否有消息可以处理，这可能会导致不必要的网络流量和 CPU 使用率增加。
     * 为了优化这一点，可以考虑使用定时任务来减少轮询频率，或者采用其他解决方案，如使用 Redis 的 PUB/SUB 结合定时任务，
     * 或者使用第三方消息队列系统（如 RabbitMQ 或 Kafka）等，它们通常提供了更加完善的延时队列支持。
     * 此外，还有一些开源项目如 DelayQueue 提供了更完整的解决方案，可以考虑使用这些工具来简化开发过程。
     *
     * @param queueName
     * @param task      the task
     * @return set
     */
    public Set<Object> dequeue(String queueName, String task) {
        Set<Object> messageSet = redisTemplate.opsForZSet().rangeByScore(queueName, 0, Instant.now().getEpochSecond());
        if (!CollectionUtils.isEmpty(messageSet)) {
            redisTemplate.opsForZSet().removeRange(queueName, 0, Instant.now().getEpochSecond());
        }
        return messageSet;
    }

}
