package org.pencil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pencil.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * The type Redis test.
 *
 * @author pencil
 * @Date 24 /08/13
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RedisConfig.class)
public class RedisTest {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 测试管道
     */
    @Test
    public void test() {

        List<Object> resultList = stringRedisTemplate.executePipelined((RedisCallback<?>) connection -> {

            connection.set("key1".getBytes(StandardCharsets.UTF_8), "value1".getBytes(StandardCharsets.UTF_8));
            connection.set("key2".getBytes(StandardCharsets.UTF_8), "value2".getBytes(StandardCharsets.UTF_8));
            connection.set("key3".getBytes(StandardCharsets.UTF_8), "value3".getBytes(StandardCharsets.UTF_8));
            connection.set("key4".getBytes(StandardCharsets.UTF_8), "value4".getBytes(StandardCharsets.UTF_8));
            connection.set("key5".getBytes(StandardCharsets.UTF_8), "value5".getBytes(StandardCharsets.UTF_8));
            connection.get("key1".getBytes(StandardCharsets.UTF_8));
            return null;
        });
        System.out.println(resultList);

    }


}
