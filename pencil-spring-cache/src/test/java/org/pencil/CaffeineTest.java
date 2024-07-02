package org.pencil;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.pencil.config.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author pencil
 * @Date 24/07/02
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CacheConfig.class)
public class CaffeineTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCachePut() {
        Cache userCache = cacheManager.getCache("user");

        assert userCache != null;
        userCache.put("name", "zhangSan");

        String name = userCache.get("name", String.class);

        Assertions.assertEquals("zhangSan", name);
    }


}
