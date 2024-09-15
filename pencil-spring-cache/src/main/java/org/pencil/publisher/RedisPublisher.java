package org.pencil.publisher;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

/**
 * @author pencil
 * @Date 24/08/19
 */
@AllArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ChannelTopic channelTopic;

    public void sendMessage(String message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }

}
