package org.pencil.subscriber;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pencil
 * @Date 24/08/19
 */
@Slf4j
public class RedisSubscriber {

    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
    }

}
